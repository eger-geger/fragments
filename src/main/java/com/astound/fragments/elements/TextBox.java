package com.astound.fragments.elements;

import com.astound.fragments.PageContext;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Keys;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Arrays;

public class TextBox extends Fragment {

    public TextBox(PageContext pageContext) {
        super(pageContext);
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
        publishEvent(String.format("entering text %s", Arrays.toString(text)));
        super.clear();
        super.sendKeys(text);
    }

    public void setTextUsingClipboard(String text) {
        publishEvent(String.format("copying text [%s] from clipdoard", text));

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