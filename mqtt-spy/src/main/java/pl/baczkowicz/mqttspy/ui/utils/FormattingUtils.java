/***********************************************************************************
 * 
 * Copyright (c) 2014 Kamil Baczkowicz
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 
 *    Kamil Baczkowicz - initial API and implementation and/or initial documentation
 *    
 */
package pl.baczkowicz.mqttspy.ui.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.baczkowicz.mqttspy.configuration.generated.ConversionFormatterDetails;
import pl.baczkowicz.mqttspy.configuration.generated.ConversionMethod;
import pl.baczkowicz.mqttspy.configuration.generated.FormatterDetails;
import pl.baczkowicz.mqttspy.configuration.generated.FormatterFunction;
import pl.baczkowicz.mqttspy.configuration.generated.SubstringConversionFormatterDetails;
import pl.baczkowicz.mqttspy.configuration.generated.SubstringExtractFormatterDetails;
import pl.baczkowicz.mqttspy.configuration.generated.SubstringFormatterDetails;
import pl.baczkowicz.mqttspy.configuration.generated.SubstringReplaceFormatterDetails;
import pl.baczkowicz.mqttspy.exceptions.ConversionException;
import pl.baczkowicz.mqttspy.utils.ConversionUtils;

/**
 * Formatting-related utils.
 */
public class FormattingUtils
{
	/** Diagnostic logger. */
	private final static Logger logger = LoggerFactory.getLogger(FormattingUtils.class);

	/**
	 * Formats the given number into one where thousands are separated by a space.
	 * 
	 * @param number The number to format
	 * @return Formatted string (e.g. "1 315 124" for 1315124)
	 */
	public static String formatNumber(final long number)
	{
		long divided = number;
		final StringBuffer sb = new StringBuffer();
		
		while (divided > 1000)
		{
			long rest = divided % 1000;
			sb.insert(0, " " + String.format("%03d", rest));
			
			divided = divided / 1000;
		}
		
		long rest = divided % 1000;
		sb.insert(0, rest);
		
		return sb.toString();
	}
	
	/**
	 * Formats the given text using the provided formatter.
	 *  
	 * @param customFormatter The formatter details
	 * @param text The text to be formatted
	 * 
	 * @return The formatted text
	 */
	public static String formatText(final FormatterDetails customFormatter, final String text)
	{
		logger.trace("Formatting '" + text + "' with " + customFormatter.getName());
		String formattedText = text;

		for (final FormatterFunction function : customFormatter.getFunction())
		{
			if (function.getSubstringReplace() != null)
			{
				formattedText = doSubstringReplacement(function.getSubstringReplace(), formattedText);
			}
			else if (function.getSubstringExtract() != null)
			{
				formattedText = doSubstringExtract(function.getSubstringExtract(), formattedText);
			}
			else if (function.getSubstringConversion() != null)
			{
				formattedText = doSubstringConversion(function.getSubstringConversion(), formattedText);
			}
			else if (function.getConversion() != null)
			{
				formattedText = convertText(function.getConversion().getFormat(), formattedText);
			}
			else if (function.getCharacterReplace() != null)
			{
				formattedText = replaceCharacters(function.getCharacterReplace().getFormat(), formattedText, 
							function.getCharacterReplace().getCharacterRangeFrom(), function.getCharacterReplace().getCharacterRangeTo(), 
							function.getCharacterReplace().getWrapCharacter());				
			}
				
			
			logger.trace("After function transformation = '" + formattedText + "'");
		}

		return formattedText;
	}
	
	/**
	 * Replaces characters using the given conversion method.
	 * 
	 * @param conversionMethod The conversion method to be used
	 * @param input The input text
	 * @param fromCharacter From index
	 * @param toCharacter To index
	 * @param wrap Characters to put around the converted text
	 * 
	 * @return The converted text
	 */
	public static String replaceCharacters(final ConversionMethod conversionMethod, final String input, final int fromCharacter, final int toCharacter, final String wrap)
	{
		String convertedText = input;
		
		for (int i = fromCharacter; i <= toCharacter; i++)
		{			
			final String characterToReplace = new String(Character.toChars(i));
			
			if (wrap != null)
			{
				convertedText = convertedText.replace(
					characterToReplace, 
					wrap + convertText(conversionMethod, characterToReplace) + wrap); 
			}
			else				
			{
				convertedText = convertedText.replace(
						characterToReplace, 
						convertText(conversionMethod, characterToReplace));
			}
		}
		
		return convertedText;
	}
	
	/**
	 * Extracts a substring from the given text.
	 * 
	 * @param details Details about what to extract
	 * @param text The text from which to extract
	 * 
	 * @return The extracted value
	 * 
	 * @throws ConversionException Thrown when cannot process the parameters correctly
	 */
	private static String extractValueForConversion(final SubstringFormatterDetails details, final String text) throws ConversionException
	{
		final int startTagIndex = text.indexOf(details.getStartTag());
		
		if (startTagIndex != -1)
		{
			final int endTagIndex = text.indexOf(details.getEndTag(), startTagIndex);
			
			if (endTagIndex != -1)
			{
				return text.substring(startTagIndex + details.getStartTag().length(), endTagIndex);				
			}
		}
		
		throw new ConversionException("Cannot find tags");
	}
	
	/**
	 * Replaces the given input text (optionally with the configured tags) with the given output text.
	 *  
	 * @param details The formatter details
	 * @param text The text to modify
	 * @param input The input text to replace
	 * @param output The text to replace the input with
	 * 
	 * @return The converted text
	 */
	private static String replaceTextAndTags(final SubstringFormatterDetails details, final String text, final String input, final String output)
	{
		String convertedText = text;
		
		if (details.isKeepTags())
		{
			convertedText = convertedText.replace(input, output); 
		}
		else
		{
			convertedText = convertedText.replace(details.getStartTag() + input + details.getEndTag(), output); 
		}
		
		return convertedText;		
	}
	
	/**
	 * Performs a string conversion for the given text based on the supplied formatter details.
	 * 
	 * @param details The formatter details
	 * @param text The text to format
	 * 
	 * @return The formatted text
	 */
	private static String doSubstringConversion(final SubstringConversionFormatterDetails details, final String text)
	{
		String convertedText = text;
		
		try
		{
			final String input = extractValueForConversion(details, convertedText);
			
			// The actual conversion value
			final String output = convertText(details.getFormat(), input);
			
			convertedText = replaceTextAndTags(details, convertedText, input, output);
		}
		catch (ConversionException e)
		{
			// Ignore, just use the input text as output
		}
				
		return convertedText;
	}
	
	/**
	 * Performs a string replacement for the given text based on the supplied formatter details.
	 * 
	 * @param details The formatter details
	 * @param text The text to format
	 * 
	 * @return The formatted text
	 */
	private static String doSubstringReplacement(final SubstringReplaceFormatterDetails details, final String text)
	{
		String convertedText = text;
		
		try
		{
			final String input = extractValueForConversion(details, convertedText);
			
			// The actual replacement value
			final String output = details.getReplaceWith();
			
			convertedText = replaceTextAndTags(details, convertedText, input, output);
		}
		catch (ConversionException e)
		{
			// Ignore, just use the input text as output
		}
				
		return convertedText;	
	}
	
	/**
	 * Performs a string extraction for the given text based on the supplied formatter details.
	 * 
	 * @param details The formatter details
	 * @param text The text to format
	 * 
	 * @return The formatted text
	 */
	private static String doSubstringExtract(final SubstringExtractFormatterDetails details, final String text)
	{
		String convertedText = text;
		
		try
		{
			final String input = extractValueForConversion(details, convertedText);
						
			if (details.isKeepTags())
			{
				convertedText = details.getStartTag() + input + details.getEndTag();
			}
			else
			{
				convertedText = input; 
			}
		}
		catch (ConversionException e)
		{
			// Ignore, just use the input text as output
		}
				
		return convertedText;	
	}
	
	/**
	 * Converts the given text using the supplied method.
	 * 
	 * @param method The method to use for conversion
	 * @param text The text to be converted
	 * 
	 * @return The converted text
	 */
	public static String convertText(final ConversionMethod method, final String text)
	{
		switch (method)
		{
			case PLAIN:
			{
				return text;
			}
			case HEX_ENCODE:
			{
				return ConversionUtils.stringToHex(text);
			}
			case HEX_DECODE:
			{
				return ConversionUtils.hexToStringNoException(text);
			}
			case BASE_64_ENCODE:
			{
				return ConversionUtils.stringToBase64(text);
			}
			case BASE_64_DECODE:
			{
				return ConversionUtils.base64ToString(text);
			}		
			default:
				return text;
		}
	}
	
	/**
	 * Formats the given text using the supplied format.
	 * 
	 * @param format The format to use for conversion
	 * @param text The text to be formatted
	 * 
	 * @return The formatted text
	 */
	public static String checkAndFormatText(final FormatterDetails format, final String text)
	{		
		if (format != null)
		{
			return FormattingUtils.formatText(format, text);
		}
		return text;
	}
	
	/**
	 * Creates a basic formatter function from the supplied conversion method.
	 *  
	 * @param conversionMethod The conversion method to be used
	 * 
	 * @return Created formmater function
	 */
	private static FormatterFunction createBasicFormatterFunction(final ConversionMethod conversionMethod)	
	{
		final FormatterFunction function = new FormatterFunction();
		
		final ConversionFormatterDetails conversionFormatterDetails = new ConversionFormatterDetails();
		conversionFormatterDetails.setFormat(conversionMethod);
		
		function.setConversion(conversionFormatterDetails);
		
		return function;
	}
	
	/**
	 * Creates formatter details for the given parameters.
	 * 
	 * @param id The ID of the formatter
	 * @param name The name of the formatter
	 * @param conversionMethod The conversion method
	 * 
	 * @return FormatterDetails object
	 */
	public static FormatterDetails createBasicFormatter(final String id, final String name, final ConversionMethod conversionMethod)	
	{
		final FormatterDetails formatter = new FormatterDetails();
		
		formatter.setID(id);
		formatter.setName(name);		
		formatter.getFunction().add(createBasicFormatterFunction(conversionMethod));
		
		return formatter;
	}
}
