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

import java.awt.geom.Ellipse2D;
import java.io.*;

import javax.vecmath.Point2f;

import com.visionopen.jiapei.smbasic.VO_Shape;

public class VO_Point2DDistributionModel {
	/** 2D normal distribution can be represented by rotated ellipse */
	protected Ellipse2D[]			m_VONormalizedEllipses;

    /** Initialization */
    protected void                  init()
    {
    	
    }

    /** Default constructor to create a VO_Point2DDistributionModel object */
    public VO_Point2DDistributionModel() {}

    /** Build Point Model */
    void							VO_BuildPointDistributionModel(	final VO_Shape[] allAlignedShapes )
    {
    	
    }

	/** Constrain respective single point according to its PDM */
	static void						VO_ConstrainSinglePoint(Point2f pt, final Ellipse2D ellipse)
	{
		
	}
	
	/** Scale all ellipses in this PDM */
	static void						VO_ScalePDMEllipses(final Ellipse2D[] iEllipses, 
														float scale,
														Ellipse2D[] oEllipses)
	{
		
	}

	/** Constrain a shape into Point Distribution Models */
	void							VO_ConstrainAllPoints(VO_Shape ioShape)
	{
		
	}

    /** Save Point Model, to a specified folder */
    void                        	VO_Save(final String fd)
    {
    	
    }

    /** Load all parameters */
    void                        	VO_Load(final String fd)
    {
    	
    }

    /** Load parameters for fitting */
    void                        	VO_LoadParameters4Fitting(final String fd)
    {
    	if(fd != "")
		{
    		init();
		    String folderDir = fd+"/Point2DDistributionModel";
		    String filename = new String();
		    FileReader filereader = null;
		    BufferedReader in = null;
		    String str = new String();
		    String ss[] = null;
		    
		    int NbOfPoints = 0;
			// Point2DDistributionModel
		    try {
			    filename = folderDir + "/Point2DDistributionModel.txt";
			    filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();
		        str = in.readLine();
		        NbOfPoints = Integer.parseInt(str);
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
			
		    // m_VONormalizedEllipses
			try {
			    filename = folderDir + "/m_VONormalizedEllipses.txt";
			    filereader = new FileReader(filename);
		        in = new BufferedReader(filereader);
		        str = in.readLine();
		        this.m_VONormalizedEllipses = new Ellipse2D[NbOfPoints];
		        for(int i = 0; i < NbOfPoints; i++)
		        {
		        	if ( (str = in.readLine())!= null)
			        {
			        	ss = str.split(" ");
//				        	this.m_VONormalizedEllipses[i];
			        }
		        }
			} catch (IOException e) {
		    	e.printStackTrace();
		    }
		}
    }
	
	// Gets and Sets
	Ellipse2D[]						GetPDMEllipses() { return this.m_VONormalizedEllipses; }

}
