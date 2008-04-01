/*
 * $Id: TestRenderers_1.java,v 1.9 2002/06/20 20:20:12 jvisvanathan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// TestRenderers_1.java

package com.sun.faces.renderkit.html_basic;

import org.apache.cactus.WebRequest;
import com.sun.faces.JspFacesTestCase;
import com.sun.faces.FacesTestCaseService;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.ResponseWriter;
import javax.faces.FacesException;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UITextEntry;
import javax.faces.component.UIForm;
import javax.faces.component.UICommand;
import javax.faces.component.UISelectOne;
import javax.faces.component.SelectItem;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIComponent;
import javax.faces.event.FormEvent;
import javax.faces.event.CommandEvent;
import javax.faces.event.FacesEvent;
import com.sun.faces.RIConstants;
import com.sun.faces.renderkit.html_basic.HtmlBasicRenderKit;
import com.sun.faces.tree.XmlTreeImpl;

import com.sun.faces.renderkit.html_basic.InputRenderer;
import com.sun.faces.renderkit.html_basic.FormRenderer;
import com.sun.faces.renderkit.html_basic.ButtonRenderer;
import com.sun.faces.renderkit.html_basic.TextAreaRenderer;
import com.sun.faces.renderkit.html_basic.RadioRenderer;
import com.sun.faces.FileOutputResponseWriter;

/**
 *
 *  Test encode and decode methods in Renderer classes.
 *
 * <B>Lifetime And Scope</B> <P>
 *
 * @version $Id: TestRenderers_1.java,v 1.9 2002/06/20 20:20:12 jvisvanathan Exp $
 * 
 *
 */

public class TestRenderers_1 extends JspFacesTestCase
{
    //
    // Protected Constants
    //
   public static final String PATH_ROOT = "./build/test/servers/tomcat40/webapps/test/";

   public static final String EXPECTED_OUTPUT_FILENAME = PATH_ROOT +
        "CorrectRenderersResponse";

   public static final String TEST_URI = "/faces/form/FormRenderer/";
   
    //
    // Class Variables
    //

    //
    // Instance Variables
    //
    private FacesContext facesContext = null;
    
    //
    // Attribute Instance Variables
    // Relationship Instance Variables
    //
    // Constructors and Initializers    
    //

    public TestRenderers_1() {
        super("TestRenderers_1");
    }
    
    public TestRenderers_1(String name) {
        super(name);
    }
   
    //
    // Class methods
    //

    //
    // Methods from TestCase
    
    public void setUp() {
        super.setUp();
        facesContext = facesService.getFacesContext();
        assertTrue(facesContext != null);
        
        if ( facesContext.getResponseTree() == null) {
            HtmlBasicRenderKit renderKit = new HtmlBasicRenderKit();
            facesContext.getServletContext().setAttribute(RIConstants.DEFAULT_RENDER_KIT,
                renderKit);
            XmlTreeImpl xmlTree = new XmlTreeImpl(
                facesContext.getServletContext(), new UICommand(), "treeId", "");
            facesContext.setRequestTree(xmlTree);
        }
        assertTrue(facesContext.getResponseWriter() != null);
     }     

    // Methods from FacesTestCase
    public boolean sendWriterToFile() {
        return true;
    }    

    public String getExpectedOutputFilename() {
        return EXPECTED_OUTPUT_FILENAME;
    }    

    public String [] getLinesToIgnore() {
        String[] lines =  {"<FORM METHOD=\"post\" ACTION=\"/test/faces;jsessionid=92F1C50409C42051E825E4A1F3B6B856?action=form&name=FormRenderer&tree=treeId\">"};
        return lines;
    }    
    
    public void beginRenderers(WebRequest theRequest) {

        theRequest.setURL("localhost:8080", null, null, TEST_URI, null);
       // theRequest.addParameter("name", "FormRenderer");
        theRequest.addParameter("/input_renderer", "InputRenderer");
        theRequest.addParameter("/textarea_renderer", "TextAreaRenderer");
        //theRequest.addParameter("action", "form");
        theRequest.addParameter("/radio_renderer", "Two");
        theRequest.addParameter("name", "ButtonRenderer");
    } 
    
    //
    // General Methods
    //
    public void testRenderers() {
        try {
            // create a dummy root for the tree.
            UIComponentBase root = new UIComponentBase() {
	        public String getComponentType() { return "root"; } 
	    };
            root.setComponentId("root");
            verifyFormRenderer(root);
            verifyInputRenderer(root);
            verifyTextAreaRenderer(root);
            verifyRadioRenderer(root);
            verifyButtonRenderer(root);
            
            boolean result = verifyExpectedOutput();
            assertTrue("Error comparing files: diff -u "+
		       EXPECTED_OUTPUT_FILENAME +
		       " " + 
		       FileOutputResponseWriter.RESPONSE_WRITER_FILENAME, result);
        }
        catch (Throwable e) {
            e.printStackTrace();
            assertTrue(false);
            return;
        }
    }
    
    public void verifyInputRenderer(UIComponent root) throws IOException {
         boolean result = false;
         
        // Test inputRenderer.
        System.out.println("Testing InputRenderer");
        UIComponent textEntry = new UITextEntry();
        textEntry.setComponentId("input_renderer");
        root.addChild(textEntry);

        InputRenderer inputRenderer = new InputRenderer();
        // test decode method
        System.out.println("Testing decode method");
        inputRenderer.decode(facesContext, textEntry);
        assertTrue(((String)textEntry.getValue()).equals("InputRenderer"));

        // test encode method
        System.out.println("Testing encode method");
        inputRenderer.encodeBegin(facesContext, textEntry);
        facesContext.getResponseWriter().write("\n");
      
        // test supportComponentType method
        System.out.println("Testing supportsComponentType method"); 
        result = inputRenderer.supportsComponentType("javax.faces.component.Form"); 
        assertTrue(!result);

        result = inputRenderer.supportsComponentType(textEntry); 
        assertTrue(result);
    }
    
    public void verifyTextAreaRenderer(UIComponent root) throws IOException {
         boolean result = false;
         
        // Test TextAreaRenderer.
        System.out.println("Testing InputRenderer");
        UIComponent textEntry = new UITextEntry();
        textEntry.setComponentId("textarea_renderer");
        root.addChild(textEntry);

        TextAreaRenderer textAreaRenderer = new TextAreaRenderer();
        // test decode method
        System.out.println("Testing decode method");
        textAreaRenderer.decode(facesContext, textEntry);
        assertTrue(((String)textEntry.getValue()).equals("TextAreaRenderer"));

        // test encode method
        System.out.println("Testing encode method");
        textAreaRenderer.encodeBegin(facesContext, textEntry);
        facesContext.getResponseWriter().write("\n");
       
        // test supportComponentType method
        System.out.println("Testing supportsComponentType method"); 
        result = textAreaRenderer.supportsComponentType("Form"); 
        assertTrue(!result);

        result = textAreaRenderer.supportsComponentType(textEntry); 
        assertTrue(result);
    }
    
    public void verifyFormRenderer(UIComponent root) throws IOException {
         boolean result = false;
         
        // Test FormRenderer.
        System.out.println("Testing FormRenderer");
        UIComponent uiForm = new UIForm();
        uiForm.setComponentId("formRenderer");
        uiForm.setValue("FormRenderer");
        root.addChild(uiForm);

        FormRenderer formRenderer = new FormRenderer();
        // test decode method
        System.out.println("Testing decode method");
        
        formRenderer.decode(facesContext, uiForm);
        
        // make sure formEvent was queued.
        System.out.println("Testing getApplicationEvent: ");
        Iterator it = facesContext.getApplicationEvents();
        assertTrue(it != null );
        
        /* assertTrue(it.hasNext());
        FacesEvent event = (FacesEvent) it.next();
        assertTrue(event instanceof FormEvent); */
        
      
        // test encode method
        System.out.println("Testing encode method");
        formRenderer.encodeBegin(facesContext, uiForm);
        facesContext.getResponseWriter().write("\n");
        
        // test encode method
        System.out.println("Testing encodeEnd method");
        formRenderer.encodeEnd(facesContext, uiForm);
        facesContext.getResponseWriter().write("\n");
        
        // test supportComponentType method
        System.out.println("Testing supportsComponentType method"); 
        result = formRenderer.supportsComponentType("Test"); 
        assertTrue(!result);

        result = formRenderer.supportsComponentType(uiForm); 
        assertTrue(result);
        
    }
    
    public void verifyButtonRenderer(UIComponent root) throws IOException {
         boolean result = false;
         
        // Test ButtonRenderer.
        System.out.println("Testing ButtonRenderer");
        UIComponent uiCommand = new UICommand();
        uiCommand.setComponentId("buttonRenderer");
        uiCommand.setValue("ButtonRenderer");
        uiCommand.setAttribute("label", "Login");
        root.addChild(uiCommand);

        ButtonRenderer buttonRenderer = new ButtonRenderer();
        // test decode method
        System.out.println("Testing decode method");
        buttonRenderer.decode(facesContext, uiCommand);
        facesContext.getResponseWriter().write("\n");
        
        // test encode method
        System.out.println("Testing encode method");
        buttonRenderer.encodeBegin(facesContext, uiCommand);
        facesContext.getResponseWriter().write("\n");
        try {
            facesContext.getResponseWriter().flush();
        } catch (Exception e ) {
            throw new FacesException("Exception while flushing buffer");
        } 

        // test supportComponentType method
        System.out.println("Testing supportsComponentType method"); 
        result = buttonRenderer.supportsComponentType("Test"); 
        assertTrue(!result);

        result = buttonRenderer.supportsComponentType(uiCommand); 
        assertTrue(result);
    }
    
    public void verifyRadioRenderer(UIComponent root) throws IOException {
         boolean result = false;
         
        // Test RadioRenderer.
        System.out.println("Testing RadioRenderer");
        UISelectOne uiSelectOne = new UISelectOne();
        uiSelectOne.setComponentId("radio_renderer");
        root.addChild(uiSelectOne);

        SelectItem item1 = new SelectItem("One", "One",null);
        SelectItem item2 = new SelectItem("Two", "Two", null);
        SelectItem item3 = new SelectItem("Three", "Three" ,null);
        
        SelectItem[] items = {item1, item2,item3};
        uiSelectOne.setItems(items);
        
        RadioRenderer radioRenderer = new RadioRenderer();
        // test decode method
        System.out.println("Testing decode method");
        radioRenderer.decode(facesContext, uiSelectOne);
        assertTrue(((String)uiSelectOne.getValue()).equals("Two"));

        // test encode method
        System.out.println("Testing encode method");
        radioRenderer.encodeBegin(facesContext, uiSelectOne);
       
        // test supportComponentType method
        System.out.println("Testing supportsComponentType method"); 
        result = radioRenderer.supportsComponentType("Test"); 
        assertTrue(!result);

        result = radioRenderer.supportsComponentType(uiSelectOne); 
        assertTrue(result);
    }
    
} // end of class TestRenderers_1
