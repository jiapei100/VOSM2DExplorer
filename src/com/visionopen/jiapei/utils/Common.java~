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
*                   VOSM is free software under the terms of the GNU Lesser General Public License  *
*                   (GNU LGPL) as published by the Free Software Foundation; either version 3.0 of  *
*                   the License, or (at your option) any later version.                             *
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
*                                                                                                   *                                                                                                   *
* Version:          1.0                                                                           	*
* Author:           JIA Pei                                                                         *
* Contact:          jp4work@gmail.com                                                               *
* URL:              http://www.visionopen.com                                                       *
* Create Date:      2010-11-04                                                                      *
* Modify Date:      2014-05-07                                                                      *
****************************************************************************************************/

package com.visionopen.jiapei.utils;

import java.util.Vector;
import javax.vecmath.Point2f;

import Jama.Matrix;
import nl.skybound.awt.DoublePolygon;

public class Common {
	public static final int 			textureShift = 128;

	Common() {}
	
	public static boolean IsContaining(Vector<Integer> v, int t)
	{
	    int size = v.size();
	    for ( int i = 0; i < size; i++ )
	    {
	        if ( v.get(i) == t )
	            return true;
	    }

	    return false;
	}
	
	public static float CalcMatrixElemSum(final double[][] iMatrix)
	{
		int NbOfRows = iMatrix.length;
		int NbOfCols = iMatrix[0].length;
		
		float sum = 0.0f;
		
	    for(int i = 0; i < NbOfRows; i++)
	    {
	    	for(int j = 0; j < NbOfCols; j++)
	    	{
	    		sum += iMatrix[i][j];
	    	}
	    }
	    
	    return sum;
	}
	
	public static float CalcMatrixElemSum(final Matrix iMatrix)
	{
	    return com.visionopen.jiapei.utils.Common.CalcMatrixElemSum(iMatrix.getArray() );
	}
	
	/* This is to calculate norm for float[][] */
	public static float CalcMatrixNorm(final float[][] iMatrix)
	{
		int NbOfRows = iMatrix.length;
		int NbOfCols = iMatrix[0].length;
		double[][] mArray = new double[NbOfRows][NbOfCols];
		mArray = com.visionopen.jiapei.utils.Common.float2double(iMatrix);
		Matrix m = new Matrix(mArray);
		
	    return (float)(m.norm2());
	}
	
	public static double[] float2double(final float[] iVector)
	{
		int NbOfDim = iVector.length;
		double[] res = new double[NbOfDim];
		
		for(int i = 0; i < NbOfDim; i++)
		{
			res[i] = (double)iVector[i];
		}
		
		return res;
	}
	
	public static float[] double2float(final double[] iVector)
	{
		int NbOfDim = iVector.length;
		float[] res = new float[NbOfDim];
		
		for(int i = 0; i < NbOfDim; i++)
		{
			res[i] = (float)iVector[i];
		}
		
		return res;
	}
	
	public static double[][] float2double(final float[][] iMatrix)
	{
		int NbOfRows = iMatrix.length;
		int NbOfCols = iMatrix[0].length;
		double[][] res = new double[NbOfRows][NbOfCols];
		
		for(int i = 0; i < NbOfRows; i++)
		{
			for(int j = 0; j < NbOfCols; j++)
				res[i][j] = (double)iMatrix[i][j];
		}
		
		return res;
	}
	
	public static float[][] double2float(final double[][] iMatrix)
	{
		int NbOfRows = iMatrix.length;
		int NbOfCols = iMatrix[0].length;
		float[][] res = new float[NbOfRows][NbOfCols];
		
		for(int i = 0; i < NbOfRows; i++)
		{
			for(int j = 0; j < NbOfCols; j++)
				res[i][j] = (float)iMatrix[i][j];
		}
		
		return res;
	}
	
	public static float[] reshapeTo1D(final float[][] iMatrix)
	{
		int NbOfRows = iMatrix.length;
		int NbOfCols = iMatrix[0].length;
		float[] res = new float[NbOfRows*NbOfCols];
		
		for(int i = 0; i < NbOfRows; i++)
		{
			for(int j = 0; j < NbOfCols; j++)
			{
				res[i*NbOfCols+j] = iMatrix[i][j];
			}
		}
		
		return res;
	}
	
	public static float[][] reshapeTo2D(final float[] iVector, int NbOfRows)
	{
		int NbOfElems = iVector.length;
		assert(NbOfElems%NbOfRows == 0);
		int NbOfCols = NbOfElems/NbOfRows;
		float[][] res = new float[NbOfRows][NbOfCols];
		
		for(int i = 0; i < NbOfRows; i++)
		{
			for(int j = 0; j < NbOfCols; j++)
			{
				res[i][j] = iVector[i*NbOfCols+j];
			}
		}
		
		return res;
	}
	
	public static float[][] reshape(final float[][] iMatrix, int NbOfRows)
	{
		float[] tmpVec = reshapeTo1D(iMatrix);
		float[][] res = reshapeTo2D(tmpVec, NbOfRows);
		
		return res;
	}
	
	public static float[] Neg(float[] iD) {
		float[] res = new float[iD.length];
		for(int i = 0; i < iD.length; i++)
			res[i] = -iD[i];
		return res;
	}
	
	public static int contains(final DoublePolygon[] triangles, final Point2f pt)
	{
		int ct = -1;
		
		for (int k = 0; k < triangles.length; k++)
		{
			// Here, we can actually give out all the local variables except the BGR, 
			// the BGR is not shape based and will be given by function VO_LoadTexturesFromShapes()
			if( triangles[k].contains(pt.x, pt.y) )
			{
				ct = k;
				break;
			}
		}
		
		return ct;
	}
	
	public float[][]				m_Mat;
    public void                 	CopyData(final float[][] iMatrix)
    {
		int NbOfRows = iMatrix.length;
		int NbOfCols = iMatrix[0].length;
    	this.m_Mat = new float[NbOfRows][NbOfCols];
    	for(int i = 0; i < iMatrix.length; i++)
    	{
        	java.lang.System.arraycopy(iMatrix[i], 0, this.m_Mat[i], 0, NbOfCols);
//    		for(int j = 0; j < NbOfPoints; j++)
//    		{
//    			this.m_MatShape[i][j] = iShape.m_MatShape[i][j];
//    		}
    	}
    }
    
    public static void main(String[] args) 
    {
    	Common test = new Common();
    	
    	float[][] inMat = new float[2][3];
    	for(int i = 0; i < inMat.length; i++)
    	{
    		for(int j = 0; j < inMat[0].length; j++)
    		{
    			inMat[i][j] = i*inMat[0].length + j;
    		}
    	}
    	System.out.println(inMat[0][0]+" "+
    			inMat[0][1]+" "+
    			inMat[0][2]+" "+
    			inMat[1][0]+" "+
    			inMat[1][1]+" "+
    			inMat[1][2]+" ");

    	test.CopyData(inMat);
    	System.out.println(test.m_Mat[0][0]+" "+
    			test.m_Mat[0][1]+" "+
    			test.m_Mat[0][2]+" "+
    			test.m_Mat[1][0]+" "+
    			test.m_Mat[1][1]+" "+
    			test.m_Mat[1][2]+" ");
    }
}
