package com.astound.fragments.elements;

import com.astound.fragments.FragmentContext;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Keys;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class TextBox extends Fragment {

	public TextBox(FragmentContext fragmentContext) {
		super(fragmentContext);
	}

	public void setText(int number) {
		setText(String.valueOf(number));
	}

	public void setText(long number) {
		setText(String.valueOf(number));
	}

	public void setText(float number) {
		setText(String.valueOf(number));
	}

	public void setText(double number) {
		setText(String.valueOf(number));
	}

	public void setText(String... text) {
		super.clear();
		super.sendKeys(text);
	}

	public void setTextUsingClipboard(String text) {
		StringSelection stringSelection = new StringSelection(text);
		Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
		clip.setContents(stringSelection, stringSelection);

		sendKeys(Keys.CONTROL + "v");
	}

	public boolean isEmpty() {
		return StringUtils.isBlank(getAttribute("value"));
	}

	public int getCharCount() {
		return getAttribute("value").length();
	}

}