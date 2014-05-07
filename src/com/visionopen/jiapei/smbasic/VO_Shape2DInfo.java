/********************************************************************************
*                                                                           	*
*   IMPORTANT: READ BEFORE DOWNLOADING, COPYING, INSTALLING OR USING.       	*
*                                                                           	*
*   By downloading, copying, installing or using the software you agree to this *
*   license. If you do not agree to this license, do not download, install,    	*
*   copy or use the software.                                      				*
*                                                                           	*
*                           	License Agreement                              	*
*          			For Vision Open Statistical Model 2D Explorer               *
*                                                                           	*
*   Copyright (C):      2010~2012 by JIA Pei, all rights reserved.          	*
*                                                                           	*
*   VOSM2DExplorer is free software under the terms of the GNU Lesser General	*
*   Public License (GNU LGPL) as published by the Free Software Foundation; 	*
*   either version 3.0 of the License, or (at your option) any later version.  	*
*   You can use it, modify it, redistribute it, etc; and redistribution and use	*
*   in source and binary forms, with or without modification, are permitted    	*
*   provided that the following conditions are met:								*
*                                                                           	*
*   a) Redistribution's of source code must retain this whole paragraph of  	*
*   copyright notice, including this list of conditions and all the following   *
*   contents in this  copyright paragraph.                        				*
*                                                                           	*
*   b) Redistribution's in binary form must reproduce this whole paragraph of 	*
*   copyright notice, including this list of conditions and all the following 	*
*   contents in this copyright paragraph, and/or other materials provided with 	*
*   the distribution.                                         					*
*                                                                           	*
*   c) The name of the copyright holders may not be used to endorse or promote 	*
*   products derived from this software without specific prior written      	*
*   permission.                                                     			*
*                                                                           	*
*   Any publications based on this code must cite the following 2 papers,		*
*   technical reports and on-line materials.                                	*
*   1) P. JIA, 2D Statistical Models, Technical Report of Vision Open Working  	*
*   Group, 2st Edition, October 21, 2010.                           			*
*   http://www.visionopen.com/members/jiapei/publications/pei_sm2dreport2010.pdf*
*   2) P. JIA. Audio-visual based HMI for an Intelligent Wheelchair.        	*
*   PhD thesis, University of Essex, February, 2011.                        	*
*   http://www.visionopen.com/members/jiapei/publications/pei_phdthesis2010.pdf	*
*                                                                           	*
* Version:          1.0                                                     	*
* Author:           JIA Pei                                                 	*
* Contact:          jp4work@gmail.com                                       	*
* URL:              http://www.visionopen.com                               	*
* Modified Date:    2012-05-22                                              	*
********************************************************************************/
package com.visionopen.jiapei.smbasic;

import java.io.*;


public class VO_Shape2DInfo {
    /** Which path is this point in, refer to IMM face database */
	private int        		m_iPath;

    /** Which type does this point belong to - closed or open */
	private int        		m_iType;

    /** Index of this point, in the model vertex sequence */
    private int        		m_iIndex;

    /** In the path, which point does this point connected from? */
    private int        		m_iFrom;

    /** In the path, which point does this point connected to? */
    private int        		m_iTo;
    
    private void			init()
    {
					    	this.m_iPath   = 0;
					        this.m_iType   = 0;
					        this.m_iIndex  = 0;
					        this.m_iFrom   = 0;
					        this.m_iTo     = 0;
    }

    private void        	CopyData(final VO_Shape2DInfo iShapeInfo)
    {
	                        this.m_iPath   = iShapeInfo.GetPath();
	                        this.m_iType   = iShapeInfo.GetType();
	                        this.m_iIndex  = iShapeInfo.GetIndex();
	                        this.m_iFrom   = iShapeInfo.GetFrom();
	                        this.m_iTo     = iShapeInfo.GetTo();
    }

    /** Default constructor to create a VO_Shape2DInfo object */
	public 					VO_Shape2DInfo()
    {
                        	init();
    }

    /** Copy constructor */
	public 					VO_Shape2DInfo( final VO_Shape2DInfo iShapeInfo )
    {
                        	this.CopyData(iShapeInfo);
    }

    /** operator= overloading, similar to copy constructor */
	public VO_Shape2DInfo	clone (final VO_Shape2DInfo iShapeInfo)
    {
	                        this.CopyData(iShapeInfo);
	                        return this;
    }

    /** Get path which this point is on */
	public int        		GetPath() { return this.m_iPath;}

    /** Get point type */
	public int        		GetType() { return this.m_iType;}

    /** Get point index, in the AAM model vertex sequence */
	public int        		GetIndex() { return this.m_iIndex;}

    /** Get the point index in the path, which this point connects from */
	public int        		GetFrom() { return this.m_iFrom;}

    /** Get the point index in the path, which this point connects to */
	public int        		GetTo() { return this.m_iTo;}

    /** Set path the point go through */
	public void				SetPath(int iPath) { this.m_iPath = iPath;}

    /** Set point type */
	public void				SetType(int iType) { this.m_iType = iType;}

    /** Set point index, in the AAM model vertex sequence  */
	public void				SetIndex(int iIndex) { this.m_iIndex = iIndex;}

    /** Set the point index, which this point connected from in path */
	public void				SetFrom(int iFrom) { this.m_iFrom = iFrom;}

    /** Set the point index, which this point connected to  in path */
	public void				SetTo(int iTo) { this.m_iTo = iTo;}

    // read VO_Shape2DInfo and VO_FaceParts
    public static void    	ReadShape2DInfo(final String fn, VO_Shape2DInfo[] oShapeInfo, VO_FaceParts faceparts )
    {
        try {
        	FileReader frfn = new FileReader(fn);
	        BufferedReader in = new BufferedReader(frfn);
	        String str = in.readLine();
	        int NbOfPoints = Integer.parseInt(str);
	        oShapeInfo = new VO_Shape2DInfo[NbOfPoints];
	        for(int i = 0; i < NbOfPoints; i++)
	        {
	        	oShapeInfo[i] = new VO_Shape2DInfo();
	        }
	        faceparts = new VO_FaceParts();
	        int count = 0;
	        String ss[] = null;
	        
	        while ( (str = in.readLine()).compareTo("m_vShape2DInfo") != 0 )
	        {
	        }
	        while ( (str = in.readLine())!=null )
	        {
	        	if(str.trim().length() != 0)
	        	{
	        		ss = str.split(" ");
		        	oShapeInfo[count].m_iPath 	= Integer.parseInt(ss[0]);
		        	oShapeInfo[count].m_iType 	= Integer.parseInt(ss[1]);
		        	oShapeInfo[count].m_iIndex 	= Integer.parseInt(ss[2]);
		        	oShapeInfo[count].m_iFrom 	= Integer.parseInt(ss[3]);
		        	oShapeInfo[count].m_iTo 	= Integer.parseInt(ss[4]);
		        	++count;
	        	}
	        	else
	        		break;
	        }
	        while ( (str = in.readLine()).compareTo("#FaceParts") != 0 )
	        {
	        }
	        
	        while ( (str = in.readLine())!=null )
	        {
	        	if(str.trim().length() != 0)
	        	{
	        		VO_FacePart onePart = new VO_FacePart();
		        	onePart.ReadFacePart(str);
		        	faceparts.VO_SetOneFacePart(onePart);
	        	}
	        	else
	        		break;
	        }
	        
	        in.close();
	        frfn.close();
	        
        } catch (IOException e){
			e.printStackTrace();
		}
    }
    
    public static void main(String args[]) {
    	String fn = "/home/jiapei/MyPrograms/Eclipse/AAMJava3D/traineddata/2D/JIAPei/ShapeModel/m_vShape2DInfo.txt";
    	VO_Shape2DInfo[] shapeinfos = null;
    	VO_FaceParts fps = null;
    	VO_Shape2DInfo.ReadShape2DInfo(fn, shapeinfos, fps );
	}
}
