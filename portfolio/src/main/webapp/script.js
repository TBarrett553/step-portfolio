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

google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);

/**
 * Adds a randomized donation link to the guest book portion.
 */
function addDonation() {
  const donations =[
        '<a href="https://www.floridajc.org/">Florida Justice Center</a>', 
        '<a href="https://actionnetwork.org/fundraising/louisville-community-bail-fund/">Louisville Community Bail Fund</a>', 
        '<a href="https://austinjustice.org/">Austin Justice Coalition</a>',
        '<a href="https://blacklivesmatters.carrd.co/">Black Lives Matter</a>', 
        '<a href="https://google.benevity.org/campaigns/1643">Google Give</a>', 
        '<a href=""https://www.blackvisionsmn.org/">Black Visions Collective</a>'
    ];

  // Pick a random donation link.
  const donation = donations[Math.floor(Math.random() * donations.length)];

  // Add it to the page.
  const donationContainer = document.getElementById('donate-container');
  donationContainer.innerHTML = donation;
}

function getUserComments() {
    //Gets the user comments stored in datastore but I need help with this
    fetch('/data?quantity=5').then(response => response.json()).then((messages) => {
        
        //This message appears so the data was fetched
        console.log(messages.length);

        //I'm not sure how to display the comments.
        comments = document.getElementById('comment-container');
        var comments_display = "";

        var counter;
        for (counter = 0; counter < messages.length; counter++){
            comments_display += messages[counter] + "<br>";    
        }
        comments.innerHTML = comments_display;
    });
}

function deleteComments() {
    const request = new Request('/delete-data');
    fetch(request).then(response => response.json()).then(delmessages => {
        console.log("messages gone")
    });
}

/** Creates a chart and adds it to the page. */
function drawChart() {
  const data = new google.visualization.DataTable();
  data.addColumn('string', 'Result');
  data.addColumn('number', 'Percent');
        data.addRows([
          ['No Charges', 347],
          ['Charged', 3],
          ['Convicted', 1]
        ]);

  const options = {
    'title': 'U.S. Police Accountability (2013-2019)',
    'width':500,
    'height':400
  };

  const chart = new google.visualization.PieChart(
      document.getElementById('chart-container'));
  chart.draw(data, options);
}