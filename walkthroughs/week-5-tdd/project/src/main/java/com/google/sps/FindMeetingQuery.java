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

package com.google.sps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Set;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    // Initiatize return data structure and set duration parameter
    Collection<TimeRange> availTimes = new HashSet<>();
    ArrayList<TimeRange> bookTimes = new ArrayList<TimeRange>();
    final int length = (int) request.getDuration();
    Collection<String>  requestAttendees = request.getAttendees();
    
    // Create a List of relevant events that will restrict available time ranges
    bookTimes = findTimeConflicts(requestAttendees, events);

    // Check that there are booked meetings before sorting it and running the available function
    if (bookTimes.isEmpty()) {
            availTimes.add(bookTimes.get(0).WHOLE_DAY);
    }

    else {
        Collections.sort(bookTimes, bookTimes.get(0).ORDER_BY_START);
        availTimes = availableTimes(bookTimes, length);
    }

    return availTimes;
  }

  public ArrayList<TimeRange> findTimeConflicts(Collection<String> requestAttendees, Collection<Event> events){
    // Create a List of relevant events that will restrict available time ranges
    ArrayList<TimeRange> bookTimes = new ArrayList<TimeRange>();

    for (Iterator<Event> eventIterator = events.iterator(); eventIterator.hasNext();){
        Event event = eventIterator.next();
      
        for (Iterator<String> attendIterator = requestAttendees.iterator(); attendIterator.hasNext();){
            String attendee = attendIterator.next();
           
            if(event.getAttendees().contains(attendee) && !bookTimes.contains(event.getWhen())){
                bookTimes.add(event.getWhen());
            }
        }
    }
    return bookTimes;
  }

  public Collection<TimeRange> availableTimes(ArrayList<TimeRange> bookTimes, int length){
    Collection<TimeRange> availTimes = new HashSet<>();
    if(length > bookTimes.get(0).END_OF_DAY) {
        return availTimes;
    }
// My problems are in the for loop.I believe that my conditionals are inefficient and I'm still working on it.
    else {
    
        for(ListIterator<TimeRange> bookIterator = bookTimes.listIterator(); bookIterator.hasNext();) {
            TimeRange bookedTime = bookIterator.next();
            
            if(bookIterator.hasPrevious()) {
                TimeRange prevBookedTime = bookIterator.previous();
                if(!bookedTime.overlaps(prevBookedTime) && (prevBookedTime.end() + length <= bookedTime.start())) {
                    availTimes.add(bookedTime.fromStartEnd(prevBookedTime.end(), bookedTime.start(), false));
                }

                else if(bookedTime.overlaps(prevBookedTime)) {
                    if(prevBookedTime.contains(bookedTime)){
                    availTimes.add(bookedTime.fromStartEnd(prevBookedTime.end(), bookedTime.END_OF_DAY, true));
                    availTimes.add(bookedTime.fromStartEnd(prevBookedTime.START_OF_DAY, bookedTime.END_OF_DAY, true));
                    }
                    
                }

                else if(!bookIterator.hasNext()) {
                    availTimes.add(bookedTime.fromStartEnd(bookedTime.end(), bookedTime.END_OF_DAY, true));
                }  
            }
            
            else if(bookedTime.start() - length >= bookedTime.START_OF_DAY) {
                availTimes.add(bookedTime.fromStartEnd(bookedTime.START_OF_DAY, bookedTime.start(), false));
                if(!bookIterator.hasNext()) {
                    availTimes.add(bookedTime.fromStartEnd(bookedTime.end(), bookedTime.END_OF_DAY, true));
                }
            }
            break;   
        }
    }
    return availTimes;
  }
}