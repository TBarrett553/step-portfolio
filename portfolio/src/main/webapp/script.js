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
 * Adds a random greeting to the page.
 */
function addDonation() {
  const donations =
      ['Florida Justice Center: https://www.floridajc.org/', 
      'Louisville Community Bail Fund: https://actionnetwork.org/fundraising/louisville-community-bail-fund/', 
      'Austin Justice Coalition: https://austinjustice.org/', 'Black Lives Matter: https://blacklivesmatters.carrd.co/', 
      'Google Give: https://google.benevity.org/campaigns/1643', 'Black Visions Collective: https://www.blackvisionsmn.org/'];

  // Pick a random greeting.
  const donation = donations[Math.floor(Math.random() * donations.length)];

  // Add it to the page.
  const donationContainer = document.getElementById('donate-container');
  donationContainer.innerText = donation;
}
