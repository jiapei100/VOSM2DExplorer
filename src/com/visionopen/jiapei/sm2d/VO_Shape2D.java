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
* Version:          1.1                                                                             *
* Author:           JIA Pei                                                                         *
* Contact:          jiapei@longervision.com                                                         *
* URL:              http://www.longervision.com                                                     *
* Create Date:      2010-12-25                                                                      *
* Modify Date:      2014-05-07                                                                      *
* Modify Date:      2021-05-07                                                                      *
****************************************************************************************************/

package com.visionopen.jiapei.sm2d;


import javax.vecmath.Point2f;
import java.util.ArrayList;

import com.visionopen.jiapei.smbasic.VO_Shape;


public class VO_Shape2D extends VO_Shape{
	
	public VO_Shape2D() {super();}
	
	public VO_Shape2D(int NbOfPoints) {super(2, NbOfPoints);}
	
	public VO_Shape2D(float[][] iShape) {super(iShape); assert (iShape.length == 2);}
        
	public VO_Shape2D(final VO_Shape2D iShape)
	{
		super(iShape.m_MatShape);
	}
	
	public VO_Shape2D(final ArrayList<Point2f> pointList)
	{
		int plSize = pointList.size();
		this.m_MatShape = new float[2][plSize];
		for(int i = 0; i < plSize; i++)
		{
			this.m_MatShape[0][i] = (float)pointList.get(i).getX();
			this.m_MatShape[1][i] = (float)pointList.get(i).getY();
		}
	}
	
	public VO_Shape2D(final Point2f[] points)
	{
		int plSize = points.length;
		this.m_MatShape = new float[2][plSize];
		for(int i = 0; i < plSize; i++)
		{
			this.m_MatShape[0][i] = (float)points[i].getX();
			this.m_MatShape[1][i] = (float)points[i].getY();
		}
	}

    public VO_Shape2D(final float[] iShape)
	{
        super(iShape, 2);
    }
}

