/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2015-2017 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.hk2.xml.internal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.glassfish.hk2.utilities.general.GeneralUtilities;

/**
 * @author jwells
 *
 */
public class ChildDataModel implements Serializable {
    private static final long serialVersionUID = 208423310453044595L;
    private final static Map<String, Class<?>> TYPE_MAP = new HashMap<String, Class<?>>();
    
    static {
        TYPE_MAP.put("char", char.class);
        TYPE_MAP.put("byte", byte.class);
        TYPE_MAP.put("short", short.class);
        TYPE_MAP.put("int", int.class);
        TYPE_MAP.put("float", float.class);
        TYPE_MAP.put("long", long.class);
        TYPE_MAP.put("double", double.class);
        TYPE_MAP.put("boolean", boolean.class);
    };
    
    private final Object lock = new Object();
    
    /** Set at compile time, the type of the thing */
    private String childType;
    private String defaultAsString;
    private boolean isReference;
    private boolean isElement;
    private String childListType;
    private AliasType aliasType;
    private String aliasOf;
    
    private ClassLoader myLoader;
    private Class<?> childTypeAsClass;
    private Class<?> childListTypeAsClass;
    
    public ChildDataModel() {
    }
    
    public ChildDataModel(String childType,
            String childListType,
            String defaultAsString,
            boolean isReference,
            boolean isElement,
            AliasType aliasType,
            String aliasOf) {
        this.childType = childType;
        this.defaultAsString = defaultAsString;
        this.isReference = isReference;
        this.isElement = isElement;
        this.childListType = childListType;
        this.aliasType = aliasType;
        this.aliasOf = aliasOf;
    }
    
    public String getChildType() {
        return childType;
    }
    
    public String getChildListType() {
        return childListType;
    }
    
    public String getDefaultAsString() {
        return defaultAsString;
    }
    
    public boolean isReference() {
        return isReference;
    }
    
    public boolean isElement() {
        return isElement;
    }
    
    public AliasType getAliasType() {
        return aliasType;
    }
    
    public String getXmlAlias() {
        return aliasOf;
    }
    
    public void setLoader(ClassLoader myLoader) {
        synchronized (lock) {
            this.myLoader = myLoader;
        }
    }
    
    public Class<?> getChildTypeAsClass() {
        synchronized (lock) {
            if (childTypeAsClass != null) return childTypeAsClass;
            
            childTypeAsClass = TYPE_MAP.get(childType);
            if (childTypeAsClass != null) return childTypeAsClass;
            
            childTypeAsClass = GeneralUtilities.loadClass(myLoader, childType);
            
            return childTypeAsClass;
        }
        
    }
    
    public Class<?> getChildListTypeAsClass() {
        synchronized (lock) {
            if (childListType == null) return null;
            if (childListTypeAsClass != null) return childListTypeAsClass;
            
            childListTypeAsClass = TYPE_MAP.get(childListType);
            if (childListTypeAsClass != null) return childListTypeAsClass;
            
            childListTypeAsClass = GeneralUtilities.loadClass(myLoader, childListType);
            
            return childListTypeAsClass;
        }
        
    }
    
    @Override
    public String toString() {
        return "ChildDataModel(" + childType +
                "," + defaultAsString +
                "," + isReference +
                "," + childListType +
                "," + aliasType +
                "," + System.identityHashCode(this) + ")";
    }

}
