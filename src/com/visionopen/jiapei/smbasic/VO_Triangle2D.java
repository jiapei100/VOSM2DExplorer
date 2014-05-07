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
* Copyright (C):    2006~2014 by JIA Pei, all rights reserved.                                      *
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
****************************************************************************************************/

package com.visionopen.jiapei.smbasic;


import javax.vecmath.Point2f;

import com.visionopen.jiapei.sm2d.VO_Shape2D;

public class VO_Triangle2D extends VO_Shape2D {
    public enum ClockWiseOrNot { COUNTER_CLOCKWISE, CLOCKWISE};

    /** Cached denominator for later Jacobian calculation */
    protected float				m_dD;

    /** private function to calculate cached denominator */
    protected float 			Calc_dD()
    {
								if(this.m_MatShape[0].length != 3)
								{
									System.out.println( "This is not a triangle!");
									System.out.println( "Please ensure the shape has 3 columns");
									System.exit(1);
								}
								if(this.m_MatShape.length != 2)
								{
									System.out.println( "This triangle is not in 2D!");
									System.exit(1);
								}

								float x1, x2, x3, y1, y2, y3;
	
								x1 = this.m_MatShape[0][0];
								x2 = this.m_MatShape[0][1];
								x3 = this.m_MatShape[0][2];
								y1 = this.m_MatShape[1][0];
								y2 = this.m_MatShape[1][1];
								y3 = this.m_MatShape[1][2];
	
								return (+x2*y3-x2*y1-x1*y3-x3*y2+x3*y1+x1*y2);
    }

    /** Default constructor to create a VO_Triangle2D object */
    public VO_Triangle2D() 		{this.m_MatShape = new float[2][3]; this.m_dD = 0.0f;}

    /** Constructor to create a VO_Triangle2D object with three coordinate vertexes in vector format */
    public VO_Triangle2D(final Point2f[] iVertexes)
    {
    							super(iVertexes);
    							if(this.m_MatShape[0].length != 3)
								{
									System.out.println( "This is not a triangle!");
									System.out.println( "Please ensure the shape has 3 columns");
									System.exit(1);
								}
								if(this.m_MatShape.length != 2)
								{
									System.out.println( "This triangle is not in 2D!");
									System.exit(1);
								}
								this.m_dD 			= this.Calc_dD ();
	}
	
	/** Constructor to create a VO_Triangle2D object with three coordinate vertexes in Mat_<float> format */
    public VO_Triangle2D(final float[][] iVertexes)
    {
    							super(iVertexes);
    							if(this.m_MatShape[0].length != 3)
								{
									System.out.println( "This is not a triangle!");
									System.out.println( "Please ensure the shape has 3 columns");
									System.exit(1);
								}
								if(this.m_MatShape.length != 2)
								{
									System.out.println( "This triangle is not in 2D!");
									System.exit(1);
								}
								this.m_dD	= this.Calc_dD ();
	}

    /** operator= overloading, similar to copy constructor */
    VO_Triangle2D				clone (final VO_Triangle2D s)
    {
								super.clone(s);
								this.m_dD	= s.GetdD();
								return (this);
    }

	/** whether the triangle vertexes are list in clockwise or counter-clockwise */
	public ClockWiseOrNot 		determinant( )
	{
								float determ = (this.m_MatShape[0][1] - this.m_MatShape[0][0]) 
											* (this.m_MatShape[1][2] - this.m_MatShape[1][0]) 
											- (this.m_MatShape[0][2] - this.m_MatShape[0][0]) 
											* (this.m_MatShape[1][1] - this.m_MatShape[1][0]);
								if (determ <= 0.0)
									return ClockWiseOrNot.COUNTER_CLOCKWISE;
								else
									return ClockWiseOrNot.CLOCKWISE;
	}

    /** Get cached denominator of this triangle */
    public float                GetdD() { return this.m_dD;}

    /** Set cached denominator for this triangle */
    public void                 SetdD(float idD) { this.m_dD = idD;}
}
