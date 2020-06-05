// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    private Hashtable<String,String>  messages= new Hashtable<String, String>();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    //The Arraylist is ideal because it can be changed after it is declared

        /**ArrayList<String>  messages= new ArrayList<String>();
        messages.add("Hi there, I hope you are having a good day!");
        messages.add("Hello, find something fun to do today!");
        messages.add("Greetings! You will do great things today!");
        messages.add("Hello Teanna!");*/

    
    //Convert the string messages into JSON
        String json = convertToJsonUsingGson(messages);

    //This is very important because it sends the messages to the client
        response.setContentType("application/json;");
        response.getWriter().println(json);

    }

    private String convertToJsonUsingGson(Hashtable<String,String> messages){
        Gson gson = new Gson();
        String json = gson.toJson(messages);
        return json;
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        String userName = request.getParameter("name");
        String userComment = request.getParameter("comment");

        messages.put(userName, userComment);

        //response.setContentType("text/html");
        response.sendRedirect("/index.html");
  }
}
