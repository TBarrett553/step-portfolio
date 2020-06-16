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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import java.io.IOException;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that gets and stores user comment data.*/
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //Check if the quantity is null. This usually happens when the user leave the quantity input blank

        if (request.getParameter("quantity") == null) {
            Integer quantity = 5;
        }

        Integer quantity = Integer.parseInt(request.getParameter("quantity"));

        Query query = new Query("Comment");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);

        ArrayList<String> storedmessages = new ArrayList<String>();
        for (Entity entity : results.asIterable(FetchOptions.Builder.withLimit(quantity))) {
            String userComment = (String) entity.getProperty("comment");
            storedmessages.add(userComment);
        }
        
    // Convert the string messages into JSON
        String json = convertToJsonUsingGson(storedmessages);

    // This is very important because it sends the messages to the client
        response.setContentType("application/json;");
        response.getWriter().println(json);
    }

    private String convertToJsonUsingGson(ArrayList<String> data){
        Gson gson = new Gson();
        String json = gson.toJson(data);
        return json;
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the user input from the comment form
        String name = request.getParameter("name");
        String comment = request.getParameter("comment");

        // Place in temporary hashtable
        Hashtable<String,String>  messages = new Hashtable<String, String>();
        messages.put(name, comment);

        // Defines datastore variables that are connected to user input
        Entity commentEntity = new Entity("Comment");
        commentEntity.setProperty("name", name);
        commentEntity.setProperty("comment", comment);

        // Adds the data to the permanent datastore
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);

        // Brings the user back to the home page
        response.sendRedirect("/index.html");
  }
}
