/*
 * Copyright (c) 2018 Georgios Andreadakis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package controllers

import java.nio.file.Files

import com.google.inject.Inject
import models.DocumentViewModel
import org.tap.framework.DocumentPathSource
import org.tap.framework.parser.tika.DocumentParserTika
import play.api.mvc.{BaseController, ControllerComponents, Result}


/**
  * Controller for the file upload page.
  *
  * @author Georgios Andreadakis (georgios@andreadakis-consulting.de)
  */
class FileUploadController @Inject() (val controllerComponents: ControllerComponents)
  extends BaseController {

  def upload = Action(parse.multipartFormData) { request =>

    request.body.file("fileToImport").map { fileToImport =>
      val filename = fileToImport.filename
      filename match {
        case null => invalidFilename("No file specified")
        case "" => invalidFilename(s"Missing file named '$filename'")
        case _ =>
          val path = fileToImport.ref.path
          val doc = (new DocumentParserTika).parse(new DocumentPathSource(path))
          Files.deleteIfExists(path)
          Ok(views.html.index(filename, DocumentViewModel(doc)))
      }
    }.get
  }

  private def invalidFilename(msg: String): Result = {
    Redirect(routes.FileUploadController.error(msg))
  }

  def error(msg: String) = Action {
    Ok(views.html.error(s"TAP error: $msg"))
  }

}
