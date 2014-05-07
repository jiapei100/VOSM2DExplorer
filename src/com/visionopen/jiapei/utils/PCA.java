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

package com.visionopen.jiapei.utils;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
 
// static import of all array methods : linear algebra and statistics
import static org.math.array.LinearAlgebra.*;
import static org.math.array.StatisticSample.*;

public class PCA {
	// eigenvectors of the PC space, stored as the matrix rows
	public float[][] eigenvectors;
	// the corresponding eigenvalues; not used for PCA compression/decompression
	public float[] eigenvalues;
	// mean vector, subtracted from the projected vector
	// or added to the reconstructed vector
	public float[] mean;
	
	// default constructor
	public PCA() {}
	
	// copy constructor
	public PCA(final PCA pca)
	{
		// mean
		if(pca.mean.length>0)
		{
			this.mean = new float[pca.mean.length];
			java.lang.System.arraycopy(pca.mean, 0, this.mean, 0, this.mean.length);
		}
		
		// eigenvalues
		if(pca.eigenvalues.length>0)
		{
			this.eigenvalues = new float[pca.eigenvalues.length];
			java.lang.System.arraycopy(pca.eigenvalues, 0, this.eigenvalues, 0, this.eigenvalues.length);
		}
		
		// eigenvectors
		if(pca.eigenvectors.length>0 && pca.eigenvectors[0].length>0)
		{
			this.eigenvectors = new float[pca.eigenvectors.length][pca.eigenvectors[0].length];
			for(int i = 0; i < this.eigenvectors.length; ++i)
			{
				java.lang.System.arraycopy(pca.eigenvectors[i], 0,
										this.eigenvectors[i], 0,
										this.eigenvectors[0].length);
			}
		}
	}

	// computes PCA for a set of vectors stored as data rows or columns.
	public PCA(final float[][] data, final float[] vmean, int flags, double variancePercentage)
	{
		double[][] X = com.visionopen.jiapei.utils.Common.float2double(data);
		
		if(vmean.length > 0)
		{
			this.mean = new float[vmean.length];
			java.lang.System.arraycopy(vmean, 0, this.mean, 0, this.mean.length);
		}
		
		double[] stdevX = stddeviation(X);
		double[] meanX = mean(X);
 
		double[][] Z = new double[X.length][X[0].length];
		for (int i = 0; i < Z.length; i++)
			for (int j = 0; j < Z[i].length; j++)
				Z[i][j] = (X[i][j] - meanX[j]) / stdevX[j];
 
		double[][] cov = covariance(Z);
 
		EigenvalueDecomposition e = eigen(cov);
		double[][] evectors = e.getV().transpose().getArray();
		this.eigenvectors = com.visionopen.jiapei.utils.Common.double2float(evectors);
		double[] evalues = e.getRealEigenvalues();// covariance matrix is symetric, so only real eigenvalues.
		this.eigenvalues = com.visionopen.jiapei.utils.Common.double2float(evalues);
	}

	// projects vector into the principal components space
	public float[] project(final float[] vec)
	{
    	assert (vec.length == this.mean.length);

		double[] tempA = com.visionopen.jiapei.utils.Common.float2double(vec);
		double[][] tempB = com.visionopen.jiapei.utils.Common.float2double(this.eigenvectors);
		double[] tempC = com.visionopen.jiapei.utils.Common.float2double(this.mean);
		
		Matrix A = new Matrix( tempA, 1);
		Matrix B = new Matrix( tempB );
		Matrix C = new Matrix( tempC, 1);
		double[] D = new double [this.eigenvalues.length];
		D = (A.minus(C).times(B.transpose())).getRowPackedCopy();
		return com.visionopen.jiapei.utils.Common.double2float(D);
	}
	public void project(final float[] vec, float[] result)
	{
		float[] tmp = this.project(vec);
		java.lang.System.arraycopy(tmp, 0, result, 0, tmp.length);
	}

	// reconstructs the vector from its PC projection
	/**
	 * @author JIA Pei
	 * @param vec	Input -- shape parameters, a row vector
	 */
	public float[] backProject(final float[] vec)
	{
    	assert (vec.length == this.eigenvalues.length);

		double[] tempA = com.visionopen.jiapei.utils.Common.float2double(vec);
		double[][] tempB = com.visionopen.jiapei.utils.Common.float2double(this.eigenvectors);
		double[] tempC = com.visionopen.jiapei.utils.Common.float2double(this.mean);
		
		Matrix A = new Matrix( tempA, 1);
		Matrix B = new Matrix( tempB );
		Matrix C = new Matrix( tempC, 1);
		double[] D = new double [this.mean.length];
		D = (A.times(B).plus(C)).getRowPackedCopy();
		return com.visionopen.jiapei.utils.Common.double2float(D);
	}
	public void backProject(final float[] vec, float[] result)
	{
		float[] tmp = this.backProject(vec);
		java.lang.System.arraycopy(tmp, 0, result, 0, tmp.length);
	}
}

