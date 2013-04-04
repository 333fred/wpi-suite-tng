/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite: Team Swagasarus
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex Chen
 *******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanager.view;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

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
			&& str.matches("^0*[0-9]{1,14}$")) {
        	super.insertString(fb, off, str, attr);
        }
        else {
        	Toolkit.getDefaultToolkit().beep();
        }
    } 
	
    @Override    
    public void replace(FilterBypass fb, int offs, int len, String str, AttributeSet a)
    		throws BadLocationException 
    {
    	if ((fb.getDocument().getLength() + str.length() - len) <= maxChars 
    			&& str.matches("[0-9]{1,14}"))
    		super.replace(fb, offs, len, str, a);
    	else
    		Toolkit.getDefaultToolkit().beep();
    }

}
