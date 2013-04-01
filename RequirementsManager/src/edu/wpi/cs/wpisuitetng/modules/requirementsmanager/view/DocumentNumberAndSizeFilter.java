/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.Toolkit;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;

/**
 * @author Alex C
 *
 */
public class DocumentNumberAndSizeFilter extends DocumentFilter{
	int maxChars;

	public DocumentNumberAndSizeFilter(int maxChars){
		this.maxChars = maxChars;
	};

	@Override
    public void insertString(FilterBypass fb, int off, String str, AttributeSet attr) 
        throws BadLocationException 
    {
		if ((fb.getDocument().getLength() + str.length()) <= maxChars 
			&& str.matches("[0-9]")) {
        	super.insertString(fb, off, str, attr);
        }
        else {
        	Toolkit.getDefaultToolkit().beep();
        }
    } 
    @Override
    public void replace(FilterBypass fb, int off, int len, String str, AttributeSet attr) 
        throws BadLocationException 
    {
    	if ((fb.getDocument().getLength() + str.length()) <= maxChars 
    			&& str.matches("[0-9]")) {
        	super.insertString(fb, off, str, attr);
        }
        else {
        	Toolkit.getDefaultToolkit().beep();
        }
    }
	
}
