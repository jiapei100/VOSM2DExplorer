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

import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.*;

import javax.vecmath.Point2f;

import nl.skybound.awt.DoublePolygon;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

import Jama.Matrix;

import com.visionopen.jiapei.smbasic.VO_Shape;
import com.visionopen.jiapei.smbasic.VO_Texture;
import com.visionopen.jiapei.smbasic.VO_Triangle2DStructure;


// http://www.java2s.com/Code/Java/SWT-JFace-Eclipse/ConvertbetweenSWTImageandAWTBufferedImage.htm
public class VO_2DSMExplorer{
	// for display framework
	public static final int						nSigmas = 3;
	public static final int 					imageSizeX = 300;
	public static final int 					imageSizeY = 300;
	public static VO_2DSMExplorer 				aamexplorer;
	public static PaletteData 					palette;
	public static Display 						display;
    public static Shell 						shell;
    
	// related to the model -- non-rigid transforms
    public VO_AppearanceModel 					m_appearanceModel;
    protected float[] 							m_currentModelParameters;		// 88
    protected float[] 							m_CurrentShapeParameters;		// 22
    protected float[] 							m_CurrentTextureParameters;		// 50
    protected float[] 							m_CurrentAppearanceParameters;	// 16	
    protected VO_Shape 							m_CurrentAlignedShape;			// 2*87	( back projected from the 22 truncated shape parameters
    protected VO_Texture						m_CurrentNormalizedTexture;		// 3*40052 ( back projected from the 50 truncated texture parameters  
    protected float[] 							m_CurrentAppearance;			// 72 = 22 + 50 ( back projected from the 16 truncated concatenated parameters
    protected VO_Shape 							templateShape;
    protected VO_Shape 							currentShape;					// scaled back already 2*87
    protected VO_Texture 						currentTexture;					// scaled back already 3*40052
    protected VO_Texture 						currentShapeTexture;			// unnecessary to be 3*40052
    protected int 								currentfaceArea;				// unnecessary to be 40052

	// display 4 images, we know the image is of size 223*222
	// while we want to show the image in the center of an area of 300*300
	// so, for the image, we need to move this image
	// X direction: (300-223)/2 = 39
	// Y direction: (300-223)/2 = 39
	
	private boolean 							m_bFirstLoad;
	private boolean 							m_bSDialogStarted;
	private boolean 							m_bTDialogStarted;
	private boolean 							m_bCDialogStarted;
	
    private ImageData 							templateImageData;
    private ImageData 							shapeImageData;
    private ImageData 							textureImageData;
    private ImageData 							shapetextureImageData;
    private Image 								templateImage;
    private Image 								shapeImage;
    private Image 								textureImage;
    private Image 								shapetextureImage;
    private Canvas[] 							ImageCanvases;
    private Label 								FaceArea;
    private Group 								imageGroup;
    private Group 								operationGroup;
    private FormData 							formData;
    private GridData 							gridData;
    private FormLayout 							formLayout;
    private GridLayout 							gridLayout;
    
	private PCAParameterControllerGroup			m_PCAShapeParamSliderController;
	private PCAParameterControllerGroup			m_PCATextureParamSliderController;
	private PCAParameterControllerGroup			m_PCAAppearanceParamSliderController;
	
	private void init() {
		aamexplorer								= null;
		palette									= null;
		display									= new Display();;
		shell									= null;
		
		m_appearanceModel 						= new VO_AppearanceModel();
		m_currentModelParameters				= null;
		m_CurrentShapeParameters				= null;
		m_CurrentTextureParameters				= null;
		m_CurrentAppearanceParameters			= null;
		m_CurrentAlignedShape					= null;
		m_CurrentNormalizedTexture				= null;
		m_CurrentAppearance						= null;
		templateShape							= null;
		currentShape							= null;
		currentTexture							= null;
		currentShapeTexture						= null;
		currentfaceArea							= 0;
		
		m_bFirstLoad 							= false;
		m_bSDialogStarted						= false;
		m_bTDialogStarted						= false;
		m_bCDialogStarted						= false;
		
		templateImageData						= null;
		shapeImageData							= null;
		textureImageData						= null;
		shapetextureImageData					= null;
		templateImage							= null;
		shapeImage								= null;
		textureImage							= null;
		shapetextureImage						= null;
		ImageCanvases							= null;
		FaceArea								= null;
		imageGroup								= null;
		operationGroup							= null;
		formData								= null;
		gridData								= null;
		formLayout								= null;
		gridLayout								= null;
		m_PCAShapeParamSliderController			= null;
		m_PCATextureParamSliderController		= null;
		m_PCAAppearanceParamSliderController	= null;
	}
	
	public VO_2DSMExplorer()
	{
		init();
	}
	
	public void updateParams()
	{
		m_CurrentShapeParameters = m_PCAShapeParamSliderController.getParamValues();
		m_CurrentTextureParameters = m_PCATextureParamSliderController.getParamValues();
		m_CurrentAppearanceParameters = m_PCAAppearanceParamSliderController.getParamValues();
		
		boolean appearanceDominant = false;
		// m_CurrentAppearanceParameters is dominant
		for(int i = 0; i < m_appearanceModel.m_iNbOfAppearanceEigens; i++)
		{
			if( Math.abs(m_CurrentAppearanceParameters[i])>1.0E-10 )
			{
				appearanceDominant = true;
				break;
			}
		}
		
		if(appearanceDominant)
		{
			m_appearanceModel.VO_CParamBackProjectToSParamTParam(m_CurrentAppearanceParameters,
																m_CurrentShapeParameters,
																m_CurrentTextureParameters);
		}
	}

	public void updateViewer()
	{
		//given the updated aam model
		for(int i = 0 ; i < m_appearanceModel.m_iNbOfShapeEigens; i++)
		{
			double st = Math.sqrt(m_appearanceModel.m_PCAAlignedShape.eigenvalues[i]);
			//get the required data from the aam model, and set it
			double temp = m_CurrentShapeParameters[i]/st;
    		int selection = 100;
    		if(temp >= VO_2DSMExplorer.nSigmas)
    			selection = 201;
    		else if (temp <= -VO_2DSMExplorer.nSigmas)
    			selection = 0;
    		else
    			selection = (int) Math.rint(temp/VO_2DSMExplorer.nSigmas*100.0+100);
    		m_PCAShapeParamSliderController.sliders[i].setValues(selection, 0, 201, 1, 1, 10);
    		m_PCAShapeParamSliderController.texts[i].setText(new Integer(selection - 100).toString());
		}
		
		for(int i = 0 ; i < m_appearanceModel.m_iNbOfTextureEigens; i++)
		{
			double tt = Math.sqrt(m_appearanceModel.m_PCANormalizedTexture.eigenvalues[i]);
			//get the required data from the aam model, and set it
			double temp = m_CurrentTextureParameters[i]/tt;
			int selection = 100;
    		if(temp >= VO_2DSMExplorer.nSigmas)
    			selection = 201;
    		else if (temp <= -VO_2DSMExplorer.nSigmas)
    			selection = 0;
    		else
    			selection = (int) Math.rint(temp/VO_2DSMExplorer.nSigmas*100.0+100);
    		m_PCATextureParamSliderController.sliders[i].setValues(selection, 0, 201, 1, 1, 10);
    		m_PCATextureParamSliderController.texts[i].setText(new Integer(selection - 100).toString());
		}
		
		for(int i = 0 ; i < m_appearanceModel.m_iNbOfAppearanceEigens; i ++)
		{
			double at = Math.sqrt(m_appearanceModel.m_PCAAppearance.eigenvalues[i]);
			double temp = m_CurrentAppearanceParameters[i]/at;
			int selection = 100;
    		if(temp >= VO_2DSMExplorer.nSigmas)
    			selection = 201;
    		else if (temp <= -VO_2DSMExplorer.nSigmas)
    			selection = 0;
    		else
    			selection = (int) Math.rint(temp/VO_2DSMExplorer.nSigmas*100.0+100);
			m_PCAAppearanceParamSliderController.sliders[i].setValues(selection, 0, 201, 1, 1, 10);
			m_PCAAppearanceParamSliderController.texts[i].setText(new Integer(selection - 100).toString());
		}
		
		// only reload shape, texture, shapetexture images
		if(m_bFirstLoad)
		{
			templateShape = m_appearanceModel.GetReferenceShape();
			templateImageData = m_appearanceModel.GetIplTemplateFace();
			palette = templateImageData.palette;
			this.LoadDefaultModel();
			templateImage 	= new Image(display, templateImageData);
			ImageCanvases[0].redraw();
		}
		this.Update4ImageData();
		shapeImage 			= new Image(display, shapeImageData);
		textureImage 		= new Image(display, textureImageData);
    	shapetextureImage 	= new Image(display, shapetextureImageData);
    	
    	ImageLoader imageLoader = new ImageLoader();
    	imageLoader.data = new ImageData[] {textureImageData};
    	imageLoader.save("./texture.jpg", SWT.IMAGE_JPEG);
    	
    	ImageCanvases[1].redraw();
    	ImageCanvases[2].redraw();
    	ImageCanvases[3].redraw();
		
		//set the area
    	currentfaceArea = CalcFacefaceArea();
    	FaceArea.setText(Integer.toString(currentfaceArea));
	}
	
	public void Update4ImageData()
	{
		m_appearanceModel.VO_SParamBackProjectToAlignedShape(m_CurrentShapeParameters, m_CurrentAlignedShape, m_appearanceModel.m_iNbOfShapeDim);
		m_appearanceModel.VO_TParamBackProjectToNormalizedTexture(m_CurrentTextureParameters, m_CurrentNormalizedTexture, m_appearanceModel.m_iNbOfTextureRepresentations);
		shapeImageData 			= this.GetCurrentShapeImageData();
		textureImageData 		= this.GetCurrentTextureImageData();
		shapetextureImageData 	= this.GetCurrentShapeTextureImageData();
	}
	
	// shapeImageData will change its size real time
	public ImageData GetCurrentShapeImageData()
	{
		currentShape = new VO_Shape(m_CurrentAlignedShape);
		currentShape.Scale(m_appearanceModel.m_fAverageShapeSize);
		float[] sm = com.visionopen.jiapei.utils.Common.Neg(currentShape.Min());
		currentShape.Translate(sm);
		Rectangle rect = currentShape.GetShapeBoundRect();
		shapeImageData = new ImageData(rect.width, rect.height, templateImageData.depth, palette);
		
		Point2f pt = new Point2f();
		int NbOfPoints = currentShape.GetNbOfPoints();
		for(int i = 0; i < NbOfPoints; i++)
    	{
    		pt = currentShape.GetA2DPoint(i);
    		shapeImageData.setPixel((int)pt.x, (int)pt.y, 0xFFFFFF);
    	}
		
		return shapeImageData;
	}
	
	// textureImageData won't change its size during process
	public ImageData GetCurrentTextureImageData()
	{
		int NbOfTriangles = m_appearanceModel.m_iNbOfTriangles;
		DoublePolygon[] oShapeTriangles = new DoublePolygon[NbOfTriangles];
		for(int i = 0; i < NbOfTriangles; i++)
		{
			Point2D[] vertexes = new Point2D[3];
			oShapeTriangles[i] = new DoublePolygon();
			for(int j = 0; j < 3; j++)
			{
				Point2f pt = m_appearanceModel.m_VOReferenceShape.GetA2DPoint(m_appearanceModel.m_vTemplateTriangle2D[i].GetVertexIndex(j));
				vertexes[j] = new Point2D.Double( pt.x, pt.y );
				oShapeTriangles[i].addPoint(vertexes[j]);
			}
		}
		
		currentTexture = new VO_Texture(m_CurrentNormalizedTexture);
		currentTexture.Scale(m_appearanceModel.m_fAverageTextureStandardDeviation);
		currentTexture.Shift(com.visionopen.jiapei.utils.Common.textureShift);
		currentTexture.Clamp(0.0f, 255.0f);
		Rectangle rect = templateShape.GetShapeBoundRect();
		textureImageData = new ImageData(rect.width, rect.height, templateImageData.depth, palette);
		
		Point2f pt = new Point2f();
		// Get the image FaceTemplate, for alignment!
		int PixelsInFaceTemplate = 0;
		for (int i = 0; i < textureImageData.height; i++)
		{
			for (int j = 0; j < textureImageData.width; j++)
			{
				pt.x = (float)j;
				pt.y = (float)i;
				
//				if( m_CVMConvexHull.contains( (float)j, (float)i) ) // This is not working for concave hull
				int k = VO_Triangle2DStructure.IsPointInTriangles(pt, m_appearanceModel.m_vTemplateTriangle2D);
				if(k>=0)
				{
					//thePixel
					switch(m_appearanceModel.m_iNbOfChannels)
					{
					case 1:
					{
						int thePixel = ((int)currentTexture.GetATexture(PixelsInFaceTemplate));
						textureImageData.setPixel(j, i, thePixel);
					}
						break;
					case 3:
					{
						int thePixel = ( ((int)currentTexture.GetATexture(0, PixelsInFaceTemplate) & 0xff)<<16 )
										+( ((int)currentTexture.GetATexture(1, PixelsInFaceTemplate) & 0xff)<<8)
										+ ( (int)currentTexture.GetATexture(2, PixelsInFaceTemplate) & 0xff);
						textureImageData.setPixel(j, i, thePixel);
					}
						break;
					default:
						break;
					}
					PixelsInFaceTemplate++;
				}
				else
				{
					textureImageData.setPixel(j, i, 0x000000);
				}
			}
		}
		
		return textureImageData;
	}
	
	// shapetextureImageData will change its size real time
	public ImageData GetCurrentShapeTextureImageData()
	{
		currentShape = new VO_Shape(m_CurrentAlignedShape);
		currentShape.Scale(m_appearanceModel.m_fAverageShapeSize);
		float[] sm = com.visionopen.jiapei.utils.Common.Neg(currentShape.Min());
		currentShape.Translate(sm);
		
		Rectangle rect = currentShape.GetShapeBoundRect();
		shapetextureImageData = new ImageData(rect.width, rect.height, templateImageData.depth, palette);
		
		shapetextureImageData = VO_TextureModel.VO_WarpFromOneShapeToAnother(templateShape, currentShape, m_appearanceModel.m_vTemplateTriangle2D, textureImageData);

		return shapetextureImageData;
	}

	public int CalcFacefaceArea()
	{
		int faceArea = 0;
		
		int NbOfTriangles = m_appearanceModel.m_iNbOfTriangles;
		DoublePolygon[] oShapeTriangles = new DoublePolygon[NbOfTriangles];
		for(int i = 0; i < NbOfTriangles; i++)
		{
			Point2D[] vertexes = new Point2D[3];
			oShapeTriangles[i] = new DoublePolygon();
			for(int j = 0; j < 3; j++)
			{
				Point2f pt = currentShape.GetA2DPoint(m_appearanceModel.m_vTemplateTriangle2D[i].GetVertexIndex(j));
				vertexes[j] = new Point2D.Double( pt.x, pt.y );
				oShapeTriangles[i].addPoint(vertexes[j]);
			}
		}
		
		// calculate the 95 affine matrixes!
		Matrix [] affineMatrixes = new Matrix[NbOfTriangles];
		// make sure src -- oShape
		// dst -- iShape
		for (int i = 0; i < NbOfTriangles; i++)
		{
			affineMatrixes[i] = new Matrix(2, 3);
			double[][] srcArray = new double[3][3];
			double[][] dstArray = new double[2][3];
			for(int j = 0; j < 3; j++)
			{
				srcArray[0][j] = oShapeTriangles[i].getAt(j).getX();
				srcArray[1][j] = oShapeTriangles[i].getAt(j).getY();
				srcArray[2][j] = 1.0f;
				dstArray[0][j] = m_appearanceModel.m_VOReferenceShape.GetA2DPoint(m_appearanceModel.m_vTemplateTriangle2D[i].GetVertexIndex(j)).getX();
				dstArray[1][j] = m_appearanceModel.m_VOReferenceShape.GetA2DPoint(m_appearanceModel.m_vTemplateTriangle2D[i].GetVertexIndex(j)).getY();
			}
			Matrix src = new Matrix(srcArray);
			Matrix dst = new Matrix(dstArray);
			affineMatrixes[i] = dst.times(src.inverse());
		}
		
		faceArea = 0;
		Rectangle rect = templateShape.GetShapeBoundRect();
		
		for (int i = 0; i < rect.height; i++)
		{
			for (int j = 0; j < rect.width; j++)
			{
				boolean isThisPointContained = false;
				for (int k = 0; k < oShapeTriangles.length; k++)
				{
					// Here, we can actually give out all the local variables except the BGR, 
					// the BGR is not shape based and will be given by function VO_LoadTexturesFromShapes()
					if( oShapeTriangles[k].contains((float)j, (float)i))
					{
						isThisPointContained = true;
						break;
					}
				}
				
				if(isThisPointContained)
				{
					faceArea ++;
				}
			}
		}
				
		return faceArea;
	}
	
	// actually, it's always warp from the template shape with the current texture
	// to the current shape, so that a new texture could be generated just for display
	// iShape, input shape(known) with known input texture -- normally, the template texture
	// oShape, out shape(known) with unknown output texture -- which is to be calculated
//	public void GetWarpTextureFromImage(final VO_Shape iShape, final VO_Shape oShape, final VO_Triangle2DStructure[] triangles, final ImageData iImg, ImageData oImg)
//	{
//		int NbOfChannels	= iImg.bytesPerLine/iImg.width;
//		int NbOfTriangles 	= triangles.length;
//	    
//	    int NbOfPixels = VO_TextureModel.VO_WarpFromOneShapeToAnother(iShape, oShape, triangles, iImg, oImg);
//
//	}
	
	public Point2D GetTemplateImageCenterOfGravity()
	{
		float[] pt= templateShape.CenterOfGravity();
		return ( new Point2D.Float(pt[0], pt[1]) );
	}
	
    // start GUI
	public void start(final VO_2DSMExplorer explorer)
	{
		shell = new Shell(display);
		shell.setSize(800,600);
		shell.setText("VOSM 2D Explorer 1.1 by Dr. Nobody, Email: jiapei@longervision.com URL: http://www.longervision.com");
		
		formLayout = new FormLayout();
		formLayout.marginWidth = 3;
		formLayout.marginHeight = 3;
		shell.setLayout(formLayout);
		
		//////////////////////////////////////////////////////////////////////////
		// display 4 images, we know the image is of size 205*192
		// while we want to show the image in the center of an area of 300*300
		// so, for the image, we need to move this image
		// X direction: (300-252)/2 = 3 
		// Y direction: (300-252)/2 = 30
		imageGroup = new Group(shell, SWT.NONE);
		imageGroup.setText("AAM Model Images: LT-template LB-shape RT-texture RB-shape texture");
		formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(0, 5);
		formData.width = 600;
		formData.height = 600;
    	imageGroup.setLayoutData(formData);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		imageGroup.setLayout(gridLayout);
     	
    	// Always remember, put Center of Gravity to each canvas' center!
        ImageCanvases= new Canvas[4];
    	for(int i = 0; i < 4; i++)
    	{
    		gridData = new GridData(GridData.FILL_BOTH);
        	gridData.heightHint = imageSizeY;
        	gridData.widthHint = imageSizeX;
        	
	        ImageCanvases[i] = new Canvas(imageGroup, SWT.BORDER);
	        ImageCanvases[i].setLayoutData(gridData);
	        ImageCanvases[i].setBackground(display.getSystemColor(SWT.COLOR_BLACK));
	        
	        if(i == 0)
		    {
		        ImageCanvases[i].addPaintListener(new PaintListener() {
		            public void paintControl(PaintEvent event) {
		                 if (templateImage != null) {
		                	 float[] COG = m_appearanceModel.m_VOReferenceShape.CenterOfGravity();
		                	 int XShift = imageSizeX/2 - (int)Math.ceil( COG[0] + 0.5) ;
		                	 int YShift = imageSizeY/2 - 20 - (int)Math.ceil( COG[1] + 0.5) ;
		                     event.gc.drawImage(templateImage, XShift, YShift);
		                     
							 event.gc.setForeground(event.display.getSystemColor(SWT.COLOR_WHITE));
		                     event.gc.setLineWidth(1);
		                     for (int i = 0; i < m_appearanceModel.m_iNbOfEdges; i++)
		                     {
		                    	 int idx1 = m_appearanceModel.m_vEdge[i].GetIndex1();
		                    	 int idx2 = m_appearanceModel.m_vEdge[i].GetIndex2();
		                    	 int x1 = (int)Math.round(m_appearanceModel.m_VOReferenceShape.GetAShape(0, idx1) ) + XShift;
		                    	 int y1 = (int)Math.round(m_appearanceModel.m_VOReferenceShape.GetAShape(1, idx1) ) + YShift;
		                    	 int x2 = (int)Math.round(m_appearanceModel.m_VOReferenceShape.GetAShape(0, idx2) ) + XShift;
		                    	 int y2 = (int)Math.round(m_appearanceModel.m_VOReferenceShape.GetAShape(1, idx2) ) + YShift;
		                         // Edge
		                    	 event.gc.drawLine(x1, y1, x2, y2);
		                     }
		                     
		                     event.gc.setLineWidth(2);
		                     for (int i = 0; i < m_appearanceModel.m_iNbOfPoints; i++)
		                     {
		                    	 int px = (int)Math.round( m_appearanceModel.m_VOReferenceShape.GetAShape(0, i));
		                    	 int py = (int)Math.round( m_appearanceModel.m_VOReferenceShape.GetAShape(1, i));
		                         // Edge
		                    	 event.gc.drawLine(px, py, px, py);
		                     }
		                 }
		            }
		        });
	        }
	        
	        else if ( i == 1)
	        {
	        	//m_appearanceModel texture
		        ImageCanvases[i].addPaintListener(new PaintListener() {
		            public void paintControl(final PaintEvent event) {
		                 if (textureImage != null) {
		                	 float[] COG = m_appearanceModel.m_VOReferenceShape.CenterOfGravity();
		                	 int XShift = imageSizeX/2 - (int)Math.ceil( COG[0] + 0.5) ;
		                	 int YShift = imageSizeY/2 - 20 - (int)Math.ceil( COG[1] + 0.5) ;
		                     event.gc.drawImage(textureImage, XShift, YShift);
		                 }
		            }
		        });
	        }

	        else if ( i == 2)
	        {
	        	//m_appearanceModel shape
		        ImageCanvases[i].addPaintListener(new PaintListener() {
		            public void paintControl(final PaintEvent event) {
		                 if (shapeImage != null) {
		                	 Point2D COG = explorer.GetShapeImageCenterOfGravity();
		                	 int XShift = imageSizeX/2 - (int)Math.ceil( COG.getX() + 0.5) ;
		                	 int YShift = imageSizeY/2 - 20 - (int)Math.ceil( COG.getY() + 0.5) ;
		                     event.gc.drawImage(shapeImage, XShift, YShift);
		                 }
		            }
		        });
	        }
	        
	        else if ( i == 3)
	        {
	        	//m_appearanceModel shapetexture
		        ImageCanvases[i].addPaintListener(new PaintListener() {
		            public void paintControl(final PaintEvent event) {
		                 if (shapetextureImage != null) {
		                	 Point2D COG = explorer.GetShapeImageCenterOfGravity();
		                	 int XShift = imageSizeX/2 - (int)Math.ceil( COG.getX() + 0.5) ;
		                	 int YShift = imageSizeY/2 - 20 - (int)Math.ceil( COG.getY() + 0.5) ;
		                     event.gc.drawImage(shapetextureImage, XShift, YShift);
		                 }
		            }
		        });
	        }
    	}
    	
   	
    	operationGroup = new Group(shell, SWT.NONE);
    	operationGroup.setText("Operations");
		formData = new FormData();
		formData.top = new FormAttachment(0, 5);
		formData.left = new FormAttachment(imageGroup, 5);
		formData.width = 300;
		formData.height = 240;
		operationGroup.setLayoutData(formData);
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		operationGroup.setLayout(gridLayout);

		// label "Load a model"           
        Label tempLabel = new Label(operationGroup,  SWT.NONE);
        tempLabel.setText("Load a model:" );
        gridData = new GridData(GridData.FILL_HORIZONTAL);
    	tempLabel.setLayoutData(gridData);

    	// Load button
    	Button LoadAModelButton = new Button(operationGroup, SWT.NONE);
    	LoadAModelButton.setText("...");
        gridData = new GridData(GridData.FILL_HORIZONTAL);
    	LoadAModelButton.setLayoutData(gridData);
    	LoadAModelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String folderName = new DirectoryDialog(shell).open();
				if (folderName != null) {
					try {
						//this.LoadModelFromFiles(folderName);
				    	m_appearanceModel = new VO_AppearanceModel();
				    	m_appearanceModel.VO_LoadParameters4Fitting(folderName);
				    	m_CurrentAlignedShape = new VO_Shape();
				    	m_CurrentNormalizedTexture = new VO_Texture();
						m_CurrentShapeParameters = new float[m_appearanceModel.m_iNbOfShapeEigens];
						m_CurrentTextureParameters = new float[m_appearanceModel.m_iNbOfTextureEigens];
						m_CurrentAppearanceParameters = new float[m_appearanceModel.m_iNbOfAppearanceEigens];
				    	m_bFirstLoad = true;
				    	
				    	if(m_bSDialogStarted) m_PCAShapeParamSliderController.stop();
				    	if(m_bTDialogStarted) m_PCATextureParamSliderController.stop();
				    	if(m_bCDialogStarted) m_PCAAppearanceParamSliderController.stop();
				    	m_PCAShapeParamSliderController = new PCAParameterControllerGroup(shell, "Shape Parameters Controller");
				    	m_PCATextureParamSliderController = new PCAParameterControllerGroup(shell, "Texture Parameters Controller");
				    	m_PCAAppearanceParamSliderController = new PCAParameterControllerGroup(shell, "Appearance Parameters Controller");
				    	m_PCAShapeParamSliderController.start(aamexplorer, m_appearanceModel.m_PCAAlignedShape);
				    	m_PCATextureParamSliderController.start(aamexplorer, m_appearanceModel.m_PCANormalizedTexture);
				    	m_PCAAppearanceParamSliderController.start(aamexplorer, m_appearanceModel.m_PCAAppearance);
				    	m_bSDialogStarted = true;
				    	m_bTDialogStarted = true;
				    	m_bCDialogStarted = true;
				    	
					    updateViewer();
					    m_bFirstLoad = false;
				    } catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
    	});

		// label "Load a face"           
        tempLabel = new Label(operationGroup,  SWT.NONE);
        tempLabel.setText("Load a face:" );
        gridData = new GridData(GridData.FILL_HORIZONTAL);
    	tempLabel.setLayoutData(gridData);

    	// Load button
    	LoadAModelButton = new Button(operationGroup, SWT.NONE);
    	LoadAModelButton.setText("...");
        gridData = new GridData(GridData.FILL_HORIZONTAL);
    	LoadAModelButton.setLayoutData(gridData);
    	LoadAModelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				FileDialog fd = new FileDialog(shell);
				fd.setFilterPath("/home/jiapei/MyPrograms/Eclipse/AAMJava3D/traineddata/2D/JIAPei");
				String fileName = fd.open();
				if (fileName != null) {
					try {
						explorer.LoadModelParametersFromSingleFile(fileName);
						updateViewer();
				    } catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
    	});

		// label "Save a face"
        tempLabel = new Label(operationGroup,  SWT.NONE);
        tempLabel.setText("Save a face:" );
        gridData = new GridData(GridData.FILL_HORIZONTAL);
    	tempLabel.setLayoutData(gridData);

    	// Save button
    	Button SaveAModelButton = new Button(operationGroup, SWT.NONE);
    	SaveAModelButton.setText("...");
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        SaveAModelButton.setLayoutData(gridData);
        SaveAModelButton.addSelectionListener(new SelectionAdapter() {
             public void widgetSelected(SelectionEvent event) {
                 	String fileName = new FileDialog(shell).open();
         			if (fileName != null) {
                	  try {
                		  explorer.WriteModelParametersToSingleFile(fileName);
					} catch (Exception e) {
						e.printStackTrace();
					}
     			}
            }
        });
        
        // label "Standard face"
        tempLabel = new Label(operationGroup,  SWT.NONE);
        tempLabel.setText("Standard face:" );
        gridData = new GridData(GridData.FILL_HORIZONTAL);
    	tempLabel.setLayoutData(gridData);

    	// Default button
    	Button DefaultModelButton = new Button(operationGroup, SWT.NONE);
    	DefaultModelButton.setText("...");
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        DefaultModelButton.setLayoutData(gridData);
        DefaultModelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				try {
					explorer.LoadDefaultModel();
					updateViewer();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
        });
        
        // label "Face Area"
        tempLabel = new Label(operationGroup, SWT.NONE);
        tempLabel.setText("Face area:" );
        gridData = new GridData(GridData.FILL_HORIZONTAL);
    	tempLabel.setLayoutData(gridData);

    	// "Face Area"
    	FaceArea = new Label(operationGroup, SWT.NONE);
   		FaceArea.setText(Integer.toString(currentfaceArea));
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        FaceArea.setLayoutData(gridData);
        
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
        	if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
	}
	
	public Point2D GetShapeImageCenterOfGravity()
	{
		currentShape = new VO_Shape(m_CurrentAlignedShape);
		currentShape.Scale(m_appearanceModel.m_fAverageShapeSize);
		float[] sm = new float[2];
		sm[0] = -currentShape.MinX();
		sm[1] = -currentShape.MinY();
		currentShape.Translate(sm);
		
		float[] pt= currentShape.CenterOfGravity();
		return ( new Point2D.Float(pt[0], pt[1]) );
	}
	
	public void LoadModelParametersFromSingleFile(final String fn)
	{
		try {
			FileReader frfn = new FileReader(fn);
	        BufferedReader in = new BufferedReader(frfn);
	        
	        m_currentModelParameters = new float[m_appearanceModel.m_iNbOfShapeEigens
	                                             +m_appearanceModel.m_iNbOfTextureEigens
	                                             +m_appearanceModel.m_iNbOfAppearanceEigens];
	        
	        String str;
	        if ( (str = in.readLine())!=null)
	        {
	        	String ss[] = str.split(" ");
	        	for(int i = 0; i < ss.length; i++)
	        	{
	        		m_currentModelParameters[i] = Float.parseFloat(ss[i]);
	        	}
	        }
	        
	        this.CalcShapeTextureAppearanceParamsFromModelParams();
			this.m_appearanceModel.VO_SParamBackProjectToAlignedShape(m_CurrentShapeParameters, m_CurrentAlignedShape, 2);
			this.m_appearanceModel.VO_TParamBackProjectToNormalizedTexture(m_CurrentTextureParameters, m_CurrentNormalizedTexture, 3);
			// this.m_appearanceModel. CBackProjectToAlignedShapeNormalizedTexture();
	        
	        in.close();
	        frfn.close();
	        	
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void LoadDefaultModel()
	{
		m_currentModelParameters = new float[m_appearanceModel.m_iNbOfShapeEigens+
		                                     m_appearanceModel.m_iNbOfTextureEigens+
		                                     m_appearanceModel.m_iNbOfAppearanceEigens];

    	for(int i = 0; i < m_currentModelParameters.length; i++)
    	{
    		m_currentModelParameters[i] = 0.0f;
    	}
        
        this.CalcShapeTextureAppearanceParamsFromModelParams();
		this.m_appearanceModel.VO_SParamBackProjectToAlignedShape(m_CurrentShapeParameters, m_CurrentAlignedShape, 2);
		this.m_appearanceModel.VO_TParamBackProjectToNormalizedTexture(m_CurrentTextureParameters, m_CurrentNormalizedTexture, this.m_appearanceModel.m_iNbOfChannels);
		
//		this.LoadModelParametersFromSingleFile("fdmp.txt");
	}
	
	public void WriteDefaultModelParameters ()
	{
		WriteModelParametersToSingleFile("fdmp.txt");
	}
	
	public void WriteModelParametersToSingleFile(String fn)
	{
		try {
			FileWriter fstream = new FileWriter(fn);
	        BufferedWriter out = new BufferedWriter(fstream);
	        
	        this.CalcModelParamsFromShapeTextureAppearanceParams();
	        
	        for(int i = 0; i < m_currentModelParameters.length; i++) {
	        	out.write(m_currentModelParameters[i] + " ");
	        }
	        
	        out.close();
	        fstream.close();
	        	
		} catch (IOException e){
			e.printStackTrace();
		}
	}
		
	public void CalcShapeTextureAppearanceParamsFromModelParams()
	{
		m_CurrentShapeParameters = new float[m_appearanceModel.m_iNbOfShapeEigens];
        for(int i = 0; i < m_appearanceModel.m_iNbOfShapeEigens; i++)
        	m_CurrentShapeParameters[i] = m_currentModelParameters[i];
        
        m_CurrentTextureParameters = new float[m_appearanceModel.m_iNbOfTextureEigens];
        for(int i = 0; i < m_appearanceModel.m_iNbOfTextureEigens; i++)
        	m_CurrentTextureParameters[i] = m_currentModelParameters[m_appearanceModel.m_iNbOfShapeEigens+i];
        
        m_CurrentAppearanceParameters = new float[m_appearanceModel.m_iNbOfAppearanceEigens];
        for(int i = 0; i < m_appearanceModel.m_iNbOfAppearanceEigens; i++)
        	m_CurrentAppearanceParameters[i] = m_currentModelParameters[m_appearanceModel.m_iNbOfShapeEigens+m_appearanceModel.m_iNbOfTextureEigens+i];
	}
	
	public void CalcModelParamsFromShapeTextureAppearanceParams()
	{
        m_currentModelParameters = new float[m_appearanceModel.m_iNbOfShapeEigens
                                             +m_appearanceModel.m_iNbOfTextureEigens
                                             +m_appearanceModel.m_iNbOfAppearanceEigens];
        
        for(int i = 0; i < m_appearanceModel.m_iNbOfShapeEigens; i++)
        	m_currentModelParameters[i] = m_CurrentShapeParameters[i];
        
        for(int i = 0; i < m_appearanceModel.m_iNbOfTextureEigens; i++)
        	m_currentModelParameters[m_appearanceModel.m_iNbOfShapeEigens+i] = m_CurrentTextureParameters[i]; 
        
        for(int i = 0; i < m_appearanceModel.m_iNbOfAppearanceEigens; i++)
        	m_currentModelParameters[m_appearanceModel.m_iNbOfShapeEigens+m_appearanceModel.m_iNbOfTextureEigens+i] = m_CurrentAppearanceParameters[i];
	}
		
	public void LoadModelFromFiles(final String fd)
	{
		m_appearanceModel.VO_LoadParameters4Fitting(fd);
		m_appearanceModel.VO_LoadOthers(fd);
		m_CurrentShapeParameters = new float[m_appearanceModel.m_iNbOfShapeEigens];
		m_CurrentTextureParameters = new float[m_appearanceModel.m_iNbOfTextureEigens];
		m_CurrentAppearanceParameters = new float[m_appearanceModel.m_iNbOfAppearanceEigens];
		java.lang.System.arraycopy(m_appearanceModel.m_PCAAlignedShape.eigenvalues, 0, m_CurrentShapeParameters, 0, m_appearanceModel.m_iNbOfShapeEigens);
		java.lang.System.arraycopy(m_appearanceModel.m_PCANormalizedTexture.eigenvalues, 0, m_CurrentTextureParameters, 0, m_appearanceModel.m_iNbOfTextureEigens);
		java.lang.System.arraycopy(m_appearanceModel.m_PCAAppearance.eigenvalues, 0, m_CurrentAppearanceParameters, 0, m_appearanceModel.m_iNbOfAppearanceEigens);
		this.CalcModelParamsFromShapeTextureAppearanceParams();
		
		m_CurrentAlignedShape = new VO_Shape();
		m_CurrentNormalizedTexture = new VO_Texture();
		m_appearanceModel.VO_SParamBackProjectToAlignedShape(m_CurrentShapeParameters, m_CurrentAlignedShape, m_appearanceModel.m_iNbOfShapeDim);
		m_appearanceModel.VO_TParamBackProjectToNormalizedTexture(m_CurrentTextureParameters, m_CurrentNormalizedTexture, m_appearanceModel.m_iNbOfTextureRepresentations);
		// this.CBackProjectToAlignedShapeNormalizedTexture();
	}
}

