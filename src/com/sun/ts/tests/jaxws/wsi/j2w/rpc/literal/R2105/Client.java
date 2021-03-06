/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */

package com.sun.ts.tests.jaxws.wsi.j2w.rpc.literal.R2105;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.sharedclients.SOAPClient;
import com.sun.ts.tests.jaxws.sharedclients.rpclitclient.J2WRLSharedClient;
import com.sun.ts.tests.jaxws.wsi.constants.DescriptionConstants;
import com.sun.ts.tests.jaxws.wsi.constants.SchemaConstants;
import com.sun.ts.tests.jaxws.wsi.utils.DescriptionUtils;

public class Client extends ServiceEETest
    implements DescriptionConstants, SchemaConstants {
  /**
   * The client.
   */
  private SOAPClient client;

  private String dstr, dstr2;

  static J2WRLShared service = null;

  /**
   * Test entry point.
   *
   * @param args
   *          the command-lind arguments.
   */
  public static void main(String[] args) {
    Client test = new Client();
    Status status = test.run(args, System.out, System.err);
    status.exit();
  }

  /**
   * @class.testArgs: -ap jaxws-url-props.dat
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   *
   * @param args
   * @param properties
   *
   * @throws Fault
   */
  public void setup(String[] args, Properties properties) throws Fault {
    client = ClientFactory.getClient(J2WRLSharedClient.class, properties, this,
        service);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testSchemaTargetNamespace
   *
   * @assertion_ids: WSI:SPEC:R2105
   *
   * @test_Strategy: Retrieve the WSDL, generated by the Java-to-WSDL tool, and
   *                 examine all wsdl:types xsd:schema elements and ensure that
   *                 they have a valid, non-null targetNamespace attribute,
   *                 unless the xsd:schema element has xsd:import and/or
   *                 xsd:annotation as its only child element(s).
   *
   * 
   * @throws Fault
   */
  public void testSchemaTargetNamespace() throws Fault {
    Document document = client.getDocument();
    Element types = DescriptionUtils.getTypes(document);
    Element[] schemas = DescriptionUtils.getChildElements(types,
        XSD_NAMESPACE_URI, XSD_SCHEMA_LOCAL_NAME);
    logMsg("Number of schema elements=" + schemas.length);
    for (int i = 0; i < schemas.length; i++) {
      dstr = null;
      dstr2 = "schema[" + i + "] elements are: ";
      verifySchemaTargetNamespace(schemas[i]);
    }
  }

  protected void verifySchemaTargetNamespace(Element element) throws Fault {
    Attr attribute = element.getAttributeNode(XSD_TARGETNAMESPACE_ATTR);
    if (!containsOnlyImportOrAnnotation(element)) {
      if (attribute == null) {
        throw new Fault(
            "xsd:schema element encountered with no 'targetNamespace' attribute (BP-R2105)");
      }
      String targetNamespace = attribute.getValue();
      try {
        new URL(targetNamespace);
      } catch (MalformedURLException e) {
        throw new Fault("The targetNamespace '" + targetNamespace
            + "' is not valid (BP-R2105)", e);
      }
    }
  }

  protected boolean containsOnlyImportOrAnnotation(Element element) {
    boolean result = true;
    NodeList list = element.getChildNodes();
    for (int i = 0; i < list.getLength(); i++) {
      Node node = list.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element child = (Element) node;
        if (dstr == null)
          dstr = child.getTagName();
        else
          dstr = dstr + ", " + child.getTagName();
        String tagName = child.getTagName();
        if (tagName.indexOf(":") > -1)
          tagName = tagName.substring(tagName.indexOf(":") + 1);
        logMsg("tagName=" + tagName);
        if (!tagName.equals(XSD_IMPORT_LOCAL_NAME)
            && !tagName.equals(XSD_ANNOTATION_LOCAL_NAME)) {
          result = false;
        }
      }
    }
    logMsg(dstr2 + dstr);
    return result;
  }
}
