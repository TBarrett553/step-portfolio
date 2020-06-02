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
