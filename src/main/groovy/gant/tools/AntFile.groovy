//  Gant -- A Groovy build framework based on scripting Ant tasks.
//
//  Copyright © 2008 Russel Winder
//
//  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
//  compliance with the License. You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software distributed under the License is
//  distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
//  implied. See the License for the specific language governing permissions and limitations under the
//  License.

package gant.tools

import org.codehaus.gant.GantBinding

import org.apache.tools.ant.ProjectHelper

/**
 *  This class provides a method for including Ant XML files into a Gant run which sets up the targets from
 *  the Ant file as Gant targets.
 *
 *  @author Russel Winder <russel.winder@concertant.com>
 */
final class AntFile {
  private final GantBinding binding
  /**
   *  Constructor for the "includeTool <<" usage.
   *
   *  @param binding The <code>GantBinding</code> to bind to.
   */
  AntFile ( final GantBinding binding ) { this.binding = binding }
  /**
   *  Constructor for the "includeTool **" usage.  It is assumed that the <code>Map</code> entry provides a
   *  filename or a list of filenames of Ant XML files to load. 
   *
   *  @param binding The <code>GantBinding</code> to bind to.
   *  @param map The <code>Map</code> of initialization parameters.
   */
  AntFile ( final GantBinding binding , Map map ) {
    this.binding = binding
    includeTargets ( map.filename )
  }
  /**
   *  Read the named file assuming it is an Ant XML file.  Load the targets into the current project and
   *  then associate each of the Ant targets with a Gant target.
   *
   *  @param fileNameList the list of path to the Ant XML file.  
   */
  void includeTargets ( List fileNameList ) {
    for ( fileName in fileNameList ) { includeTargets ( fileName ) }
  }
  /**
   *  Read the named file assuming it is an Ant XML file.  Load the targets into the current project and
   *  then associate each of the Ant targets with a Gant target.
   *
   *  @param fileName the path to the Ant XML file.  
   */
  void includeTargets ( String fileName ) {
    ProjectHelper.configureProject ( binding.ant.project , new File ( fileName ) )
    binding.ant.project.targets.each { key , value ->
      assert key == value.name
      binding.setProperty ( key , { value.execute ( ) } )
      if ( value.description ) { binding.targetDescriptions.put ( key , value.description ) }
    }
  }
}
