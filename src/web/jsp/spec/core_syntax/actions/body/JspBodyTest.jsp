<%--

    Copyright (c) 2003, 2018 Oracle and/or its affiliates. All rights reserved.

    This program and the accompanying materials are made available under the
    terms of the Eclipse Public License v. 2.0, which is available at
    http://www.eclipse.org/legal/epl-2.0.

    This Source Code may also be made available under the following Secondary
    Licenses when the conditions for such availability set forth in the
    Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
    version 2 with the GNU Classpath Exception, which is available at
    https://www.gnu.org/software/classpath/license.html.

    SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0

--%>

<%@ page contentType="text/plain" %>
<%@ taglib uri="http://java.sun.com/tck/jsp/body" prefix="body" %>

<%--
     Impl note: standard actions that can accept bodies will be tested
     in the test areas for those actions.  We'll only test custom and
     standard actions here
--%>

<body:classic>
    <jsp:body>testpassed</jsp:body>
</body:classic>

<body:simple>
    <jsp:body>testpassed</jsp:body>
</body:simple>

<body:classic>
    <jsp:attribute name="attr1">value1</jsp:attribute>
    <jsp:body>testpassed</jsp:body>
</body:classic>

<body:simple>
    <jsp:attribute name="attr1">value1</jsp:attribute>
    <jsp:body>testpassed</jsp:body>
</body:simple>
