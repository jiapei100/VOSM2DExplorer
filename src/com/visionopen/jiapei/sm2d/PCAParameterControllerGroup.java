/****************************************************************************************************
*                                                                                                   *
*                   IMPORTANT: READ BEFORE DOWNLOADING, COPYING, INSTALLING OR USING.               *
*                                                                                                   *
*   By downloading, copying, installing or using the software you agree to this license.            *
*   If you do not agree to this license, do not download, install, copy or use the software.        *
*                                                                                                   *
*                                   License Agreement                                               *
*                           For Vision Open Statistical Models 2D Explorer                          *
*                                                                                                   *
* Copyright (C):    2006~2021 by JIA Pei, all rights reserved.                                      *
*                                                                                                   *
*                   VOSM2DExplorer is free software under the terms of the GNU Lesser General       *
*                   Public License (GNU LGPL) as published by the Free Software Foundation;         *
*                   either version 3.0 of the License, or (at your option) any later version.       *
*                                                                                                   *
*                   You can use it, modify it, redistribute it, etc; and redistribution and use in  *
*                   source and binary forms, with or without modification, are permitted provided   *
*                   that the following conditions are met:                                          *
*                                                                                                   *
*                   a) Redistribution's of source code must retain this whole paragraph of          *
*                   copyright notice, including this list of conditions and all the following       *
*                   contents in this copyright paragraph.                                           *
*                                                                                                   *
*                   b) Redistribution's in binary form must reproduce this whole paragraph of       *
*                   copyright notice, including this list of conditions and all the following       *
*                   contents in this copyright paragraph, and/or other materials provided with      *
*                   the distribution.                                                               *
*                                                                                                   *
*                   c) The name of the copyright holders may not be used to endorse or promote      *
*                   products derived from this software without specific prior written permission.  *
*                                                                                                   *
*                   Any publications based on this code must cite the following five papers,        *
*                   technic reports and on-line materials.                                          *
*                                                                                                   *
*                   1) P. JIA, 2D Statistical Models, Technical Report of Vision Open Working       *
*                   Group, 2st Edition, Oct 21, 2010.                                               *
*                   http://www.visionopen.com/members/jiapei/publications/pei_sm2dreport2010.pdf    *
*                                                                                                   *
*                   2) P. JIA. Audio-visual based HMI for an Intelligent Wheelchair.                *
*                   PhD thesis, University of Essex, 2010.                                          *
*                   http://www.visionopen.com/members/jiapei/publications/pei_thesischapter34.pdf   *
*                                                                                                   *
* Version:          1.0                                                                             *
* Author:           JIA Pei                                                                         *
* Contact:          jp4work@gmail.com                                                               *
* URL:              http://www.visionopen.com                                                       *
* Create Date:      2010-12-25                                                                      *
* Modify Date:      2014-05-07                                                                      *
* Modify Date:      2021-05-07                                                                      *
****************************************************************************************************/

package com.visionopen.jiapei.sm2d;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.*;

import com.visionopen.jiapei.utils.PCA;

public class PCAParameterControllerGroup {
	private Shell 					dialog;
	private MigLayout 				migLayout;
	public boolean 					start;
	protected Slider[] 				sliders;
	protected Label[]				labels;
	protected Text[] 				texts;
	protected float[]				params;
	protected int					m_iNbOfParams;
	protected PCA					m_PCA;
	protected String				m_sTitle;
	
	private static final String 	baseConstraints = "insets 0, gap 10, wrap 18, fill";
	
	public PCAParameterControllerGroup(Shell iShell, final String str) {
		this.dialog		= new Shell(iShell, SWT.DIALOG_TRIM); // SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
		this.dialog.setText(str);
		this.m_sTitle	= str;
		this.migLayout	= new MigLayout(baseConstraints);
		this.dialog.setLayout(migLayout);
		this.start 		= false;
		this.sliders	= null;
		this.labels		= null;
		this.texts		= null;
		this.m_PCA		= null;
	}
	
	public void start(final VO_2DSMExplorer aamexplorer, final PCA pca)
	{
		this.m_iNbOfParams	= pca.eigenvalues.length;
		
		this.sliders		= new Slider[this.m_iNbOfParams];
		this.labels			= new Label[this.m_iNbOfParams];
		this.texts			= new Text[this.m_iNbOfParams];
		this.params			= new float[this.m_iNbOfParams];
//		this.m_PCA			= new PCA(pca);
		this.m_PCA			= pca;
		
		for(int i = 0; i < this.m_iNbOfParams; i++)
		{
			this.labels[i] = new Label(dialog, SWT.HORIZONTAL);
		    this.labels[i].setText(this.m_sTitle.substring(0, 1) + i);
		    
			this.sliders[i] = new Slider(dialog, SWT.HORIZONTAL);
			this.sliders[i].setValues(100, 0, 201, 1, 1, 10);
			
			this.texts[i] = new Text(dialog, SWT.BORDER);
			this.texts[i].setText("0   ");
		    
		    final int index = i;
		    final Slider tmpSlider = this.sliders[index];
		    final Text tmpText = this.texts[index];
		    
		    // Refer to http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/SWTSliders.htm
		    // SWT.Selection is too time-consuming
			this.sliders[i].addListener (SWT.MouseUp, new Listener () {	// SWT.Selection
				public void handleEvent (Event event) {
					int selection = tmpSlider.getSelection() - 100;
					tmpText.setText(new Integer(selection).toString());
					aamexplorer.updateParams();
					aamexplorer.updateViewer();
				}
			});
			
			this.texts[i].addKeyListener(new KeyListener() {
				public void keyPressed(KeyEvent e) {

				}
				
				public void keyReleased(KeyEvent e) {
					if(e.keyCode == SWT.CR) {
						int selection = Integer.parseInt(tmpText.getText()) + 100;
						tmpSlider.setSelection(selection);
						aamexplorer.updateParams();
						aamexplorer.updateViewer();
					}
				}
			});
		}
		
		this.dialog.pack();
		this.dialog.open();
		
		this.start = true;
	}
	
	public float[] getParamValues()
	{
		int num = this.sliders.length;
		float[] res = new float[num];
		
		for(int i = 0; i < num; ++i)
		{
			res[i] = (float)(this.sliders[i].getSelection() - 100)/100.0f*VO_2DSMExplorer.nSigmas*(float)Math.sqrt(this.m_PCA.eigenvalues[i]);
		}
		
		return res;
	}
	
	public void stop() {
		if (start)
			dialog.close();
		start = false;
	}
	
	public static void main(String args[])
	{

	}
}
