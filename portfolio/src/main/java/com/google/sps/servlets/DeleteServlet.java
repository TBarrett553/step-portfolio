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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that deletes comments from the datastore*/
@WebServlet("/delete-data")

public class DeleteServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getParameter("quantity") == null) {
            Integer quantity = 5;
        }

        Query query = new Query("Comment");
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        Integer quantity = Integer.parseInt(request.getParameter("quantity"));

        ArrayList<String> delmessages = new ArrayList<String>();
        for (Entity entity : results.asIterable(FetchOptions.Builder.withLimit(quantity))) {
            long id = Long.parseLong(request.getParameter("id"));
            Key commentEntityKey = KeyFactory.createKey("Task", id);
            /**String comment = (String) entity.getProperty("comment");
            delmessages.add(comment);*/
            datastore.delete(commentEntityKey);
        }

        String json = convertToJsonUsingGson(delmessages);

    //This is very important because it sends the messages to the client
        response.setContentType("application/json;");
        response.getWriter().println(json);
    }

    private String convertToJsonUsingGson(ArrayList<String> data){
        Gson gson = new Gson();
        String json = gson.toJson(data);
        return json;
    }
}
