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

import java.util.Vector;

import javax.vecmath.Point2f;


import nl.skybound.awt.DoublePolygon;


public class VO_Triangle2DStructure extends VO_Triangle2D {
    /** Indexes of 3 triangle vertexes in the built AAM model vertex sequence */
    private int[]    					m_vVertexIndexes;

	/** Indexes of this triangle in the built AAM model triangle sequence */
    private int							m_iTriangleIndex;

    /** Default constructor to create a VO_Triangle2DStructure object */
    public VO_Triangle2DStructure() {this.m_vVertexIndexes = new int[3];}
    
    /** Constructor to create a VO_Triangle2DStructure object with three vertex indexes in vector format */
    public VO_Triangle2DStructure(final int[] iVertexIndexes)
    {
    	super();
        this.m_vVertexIndexes = iVertexIndexes;
    }

    /** Constructor to create a VO_Triangle2DStructure object with three vertex indexes using 3 integers */
    public VO_Triangle2DStructure(final int iIdx0, final int iIdx1, final int iIdx2)
    {
    	super();
        this.m_vVertexIndexes = new int[3];
        this.m_vVertexIndexes[0] = iIdx0;
        this.m_vVertexIndexes[1] = iIdx1;
        this.m_vVertexIndexes[2] = iIdx2;
    }

    /** Constructor to create a VO_Triangle2DStructure object with three coordinate vertexes in vector format */
    public VO_Triangle2DStructure(final Point2f[] iVertexes)
    {
    	super(iVertexes);
        this.m_vVertexIndexes = new int[3];
    }

	/** Constructor to create a VO_Triangle2DStructure object with three coordinate vertexes in a row in Mat_<float> format */
    public VO_Triangle2DStructure(final float[][] iVertexes)
    {
    	super(iVertexes);
        this.m_vVertexIndexes = new int[3];
    }

    /** Constructor to create a VO_Triangle2DStructure object with three coordinate vertexes and 
        three corresponding vertex indexes in vector format */
    public VO_Triangle2DStructure( final Point2f[] iVertexes, final int[] iVertexIndexes )
    {
    	super(iVertexes);
        assert (iVertexIndexes.length == 3);
		this.m_vVertexIndexes = iVertexIndexes;
    }

    /** Constructor to create a VO_Triangle2DStructure object with three coordinate vertexes
        and three corresponding vertex indexes in Mat_<float> format */
    public VO_Triangle2DStructure( final float[][] iVertexes, final int[] iVertexIndexes )
    {
    	super(iVertexes);
        assert (iVertexIndexes.length == 3);
		this.m_vVertexIndexes = iVertexIndexes;
    }

    /** operator= overloading, similar to copy constructor */
    public VO_Triangle2DStructure clone (final VO_Triangle2DStructure s)
    {
    	super.clone(s);
        this.m_vVertexIndexes = s.m_vVertexIndexes;
        this.m_iTriangleIndex = s.m_iTriangleIndex;
        return (this);
    }

    /** Judge whether this triangle has a vertex of index "iIndex" */
    public boolean 						HasNode(int iIndex)
    {
										if( (this.m_vVertexIndexes[0] == iIndex) 
											|| (this.m_vVertexIndexes[1] == iIndex)
											|| (this.m_vVertexIndexes[2] == iIndex) )
												return true;
										else
											return false;
    }

	/** VO_Triangle2DStructure to Mat_<float> */
    public static VO_Shape				Triangle2D2Shape(final VO_Triangle2DStructure[] triangles)
    {
    	int[] vi4oneTriangle = new int[3];
    	Vector<Integer> countedIdx = new Vector<Integer>();
    	Vector<Point2f> ptList = new Vector<Point2f>();

    	for(int i = 0; i < triangles.length; ++i)
    	{
    		vi4oneTriangle = triangles[i].GetVertexIndexes();

    		for(int j = 0; j < 3; ++j)
    		{
    			if( !com.visionopen.jiapei.utils.Common.IsContaining(countedIdx, vi4oneTriangle[j]) )
    			{
    				countedIdx.addElement(vi4oneTriangle[j]);
    				ptList.addElement( triangles[i].GetA2DPoint(j) );
    			}
    		}
    	}
    	
    	int NbOfPoints = ptList.size();
    	float[][] mat = new float[2][NbOfPoints];
    	
    	for(int i = 0; i < NbOfPoints; ++i)
    	{
    		mat[0][i] = ptList.get(i).x;
    		mat[1][i] = ptList.get(i).y;
    	}
    	return new VO_Shape(mat);
    }

	public void 						AdjustVertexSequence()
	{
		Point2f FirstVertex, SecondVertex, ThirdVertex;
		int FirstIndex, SecondIndex, ThirdIndex;

		if ( this.m_MatShape[1][0] <= this.m_MatShape[1][1])
		{
			if ( this.m_MatShape[1][0] <= this.m_MatShape[1][2])
			{
				FirstVertex	= this.GetA2DPoint(0);
				FirstIndex	= this.m_vVertexIndexes[0];
				if ( this.determinant( ) == ClockWiseOrNot.COUNTER_CLOCKWISE )
				{
					SecondVertex	= this.GetA2DPoint(1);
					ThirdVertex		= this.GetA2DPoint(2);
					SecondIndex		= this.m_vVertexIndexes[1];
					ThirdIndex		= this.m_vVertexIndexes[2];
				}
				else
				{
					SecondVertex	= this.GetA2DPoint(2);
					ThirdVertex		= this.GetA2DPoint(1);
					SecondIndex		= this.m_vVertexIndexes[2];
					ThirdIndex		= this.m_vVertexIndexes[1];
				}
			}
			else
			{
				FirstVertex = this.GetA2DPoint(2);
				FirstIndex	= this.m_vVertexIndexes[2];
				if ( this.determinant( ) == ClockWiseOrNot.COUNTER_CLOCKWISE )
				{
					SecondVertex	= this.GetA2DPoint(0);
					ThirdVertex		= this.GetA2DPoint(1);
					SecondIndex		= this.m_vVertexIndexes[0];
					ThirdIndex		= this.m_vVertexIndexes[1];
				}
				else
				{
					SecondVertex	= this.GetA2DPoint(1);
					ThirdVertex		= this.GetA2DPoint(0);
					SecondIndex		= this.m_vVertexIndexes[1];
					ThirdIndex		= this.m_vVertexIndexes[0];
				}
			}
		}
		else
		{
			if ( this.m_MatShape[1][1] <= this.m_MatShape[1][2])
			{
				FirstVertex = this.GetA2DPoint(1);
				FirstIndex	= this.m_vVertexIndexes[1];
				if ( this.determinant( ) == ClockWiseOrNot.COUNTER_CLOCKWISE )
				{
					SecondVertex	= this.GetA2DPoint(2);
					ThirdVertex		= this.GetA2DPoint(0);
					SecondIndex		= this.m_vVertexIndexes[2];
					ThirdIndex		= this.m_vVertexIndexes[0];
				}
				else
				{
					SecondVertex	= this.GetA2DPoint(0);
					ThirdVertex		= this.GetA2DPoint(2);
					SecondIndex		= this.m_vVertexIndexes[0];
					ThirdIndex		= this.m_vVertexIndexes[2];
				}
			}
			else
			{
				FirstVertex = this.GetA2DPoint(2);
				FirstIndex	= this.m_vVertexIndexes[2];
				if ( this.determinant( ) == ClockWiseOrNot.COUNTER_CLOCKWISE )
				{
					SecondVertex	= this.GetA2DPoint(0);
					ThirdVertex		= this.GetA2DPoint(1);
					SecondIndex		= this.m_vVertexIndexes[0];
					ThirdIndex		= this.m_vVertexIndexes[1];
				}
				else
				{
					SecondVertex	= this.GetA2DPoint(1);
					ThirdVertex		= this.GetA2DPoint(0);
					SecondIndex		= this.m_vVertexIndexes[1];
					ThirdIndex		= this.m_vVertexIndexes[0];
				}
			}
		}
		this.m_MatShape[0][0]		= FirstVertex.x;
		this.m_MatShape[1][0]		= FirstVertex.y;
		this.m_MatShape[0][1]		= SecondVertex.x;
		this.m_MatShape[1][1]		= SecondVertex.y;
		this.m_MatShape[0][2]		= ThirdVertex.x;
		this.m_MatShape[1][2]		= ThirdVertex.y;
		this.m_vVertexIndexes[0]	= FirstIndex;
		this.m_vVertexIndexes[1]	= SecondIndex;
		this.m_vVertexIndexes[2]	= ThirdIndex;
		this.Calc_dD();
	}

	public static int					IsPointInTriangles(final Point2f pt, final VO_Triangle2DStructure[] triangles)
	{
	    int NbOfTriangles = triangles.length;
	    DoublePolygon[] dps = new DoublePolygon[NbOfTriangles];
	    int npoints = 3;
	    double[] xpoints = new double[npoints];
	    double[] ypoints = new double[npoints];
	    
	    for(int i = 0; i < NbOfTriangles; i++)
	    {
	    	xpoints[0] = triangles[i].GetA2DPoint(0).x;
	    	xpoints[1] = triangles[i].GetA2DPoint(1).x;
	    	xpoints[2] = triangles[i].GetA2DPoint(2).x;
	    	ypoints[0] = triangles[i].GetA2DPoint(0).y;
	    	ypoints[1] = triangles[i].GetA2DPoint(1).y;
	    	ypoints[2] = triangles[i].GetA2DPoint(2).y;
	    	dps[i] = new DoublePolygon(xpoints, ypoints, 3);
	    }

		return (com.visionopen.jiapei.utils.Common.contains(dps, pt));
	}
	
    /** Get three indexes of three vertexes as a vector */
	public int[] 						GetVertexIndexes() { return this.m_vVertexIndexes;}

    /** Get one index of one vertex, in the built AAM model vertex sequence. Apparently, ptIdx could only be 0,1,2 */
	public int 							GetVertexIndex(int ptIdx) { return this.m_vVertexIndexes[ptIdx];}

    /** Get the index of this triangle, in the built AAM model triangle sequence */
	public int 							GetTriangleIndex() { return this.m_iTriangleIndex;}

    /** Set the indexes of three vertexes of this triangle */
	public void 						SetVertexIndexes(final int[] iVertexes) { this.m_vVertexIndexes = iVertexes;}

    /** Set the index of this triangle, in the built AAM model triangle sequence */
	public void 						SetTriangleIndex(int iTriangleIndex) { this.m_iTriangleIndex = iTriangleIndex;}

	public DoublePolygon 				GetATrianglePolygon()
	{
		DoublePolygon dp = new DoublePolygon();
		for(int i = 0; i < 3; i++)
			dp.addPoint(this.m_MatShape[0][i], this.m_MatShape[1][i]);
		return dp;
	}
	
}
