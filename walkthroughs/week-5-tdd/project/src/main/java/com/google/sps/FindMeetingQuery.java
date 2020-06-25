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

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {

    // Initiatizes return data structure and set duration parameter
    //Collection<TimeRange> availTimes = new HashSet<>();
    ArrayList<TimeRange> bookTimes = new ArrayList<TimeRange>();
    final int length = (int) request.getDuration();
    Collection<String>  requestAttendees = request.getAttendees();
    
    // Creates a List of relevant events that will restrict available time ranges
    bookTimes = findTimeConflicts(requestAttendees, events);

    // Collapse overlaping relevant events into one Time Range
    ArrayList<TimeRange> collapsedBookTimes = collapseTimeConflicts(bookTimes);
  
    return getAvailableTimeRanges(collapsedBookTimes, length);
  }

/**Create an ArrayList of relevant events that will restrict available time ranges */
  public ArrayList<TimeRange> findTimeConflicts(Collection<String> requestAttendees, Collection<Event> events){
    // Set doesn't store duplicates and makes the for loop below more efficient
    HashSet<TimeRange> bookTimes = new HashSet<TimeRange>();

    // Iterates thorugh each event and decides if the TimeRange is relevant based on requestAttendees
    for (Iterator<Event> eventIterator = events.iterator(); eventIterator.hasNext();){
        Event event = eventIterator.next();
        
        for (Iterator<String> attendIterator = requestAttendees.iterator(); attendIterator.hasNext();){
            String attendee = attendIterator.next();
           
            if(event.getAttendees().contains(attendee)){
                bookTimes.add(event.getWhen());
            }
        }
    }

    // The times have to be returned as a list to be sorted by start time 
    return new ArrayList(bookTimes);
  }

  public ArrayList<TimeRange> collapseTimeConflicts (ArrayList<TimeRange> bookTimes){
        // Sorts time conflicts in chronological order
        Collections.sort(bookTimes, TimeRange.ORDER_BY_START);

        TimeRange prevBookedTime = null;
        for(ListIterator<TimeRange> bookIterator = bookTimes.listIterator(); bookIterator.hasNext();){
            TimeRange bookedTime = bookIterator.next();
            if(prevBookedTime != null){
                // When bookedTime is fully contained in the previous event, keep the longer event's Time Range
                if(prevBookedTime.contains(bookedTime)){
                    bookTimes.remove(bookedTime); 

                // For overlapping events create a new Time Range with the earliest start time and latest end time
                } else if(prevBookedTime.overlaps(bookedTime)){
                    bookTimes.add(bookedTime.fromStartEnd(prevBookedTime.start(), bookedTime.end(), false));
                    bookTimes.remove(prevBookedTime);
                    bookTimes.remove(bookedTime); 
                }
            }
            prevBookedTime = bookedTime;
        }
        return bookTimes;
  }

  public Collection<TimeRange> getAvailableTimeRanges(ArrayList<TimeRange> bookTimes, int length){
    ArrayList<TimeRange> availTimes = new ArrayList<TimeRange>();

    // There is a test that doesn't allow a duration longer than the day. The proper result is to return an empty list
    if(length > TimeRange.END_OF_DAY) {
        return availTimes;

    } 
    // If there are no Time Conflicts the whole day should be returned as available
    if (bookTimes.isEmpty()) {
        availTimes.add(TimeRange.WHOLE_DAY);
        return availTimes;

    }
    // Even though previous() is not used ListIterator must be used with iterating through ArrayList
    TimeRange prevBookedTime = null;
    for(ListIterator<TimeRange> bookIterator = bookTimes.listIterator(); bookIterator.hasNext();) {
        TimeRange bookedTime = bookIterator.next();

        /** For the last event, if there is enough room the available time can last to the end of the day. 
        This doesn't depend on the value of prevBookedTime.*/
        if(!bookIterator.hasNext() && bookedTime.end() + length <= TimeRange.END_OF_DAY ) {
            availTimes.add(bookedTime.fromStartEnd(bookedTime.end(), TimeRange.END_OF_DAY, true));
        } 

        if(prevBookedTime != null) { 
            // Determines if a gap between meetings can fit a meeting request
            if(prevBookedTime.end() + length <= bookedTime.start()) {
                availTimes.add(bookedTime.fromStartEnd(prevBookedTime.end(), bookedTime.start(), false));
            }
        // If there isn't a previous event and the request duration fits, time is available until the start of the day
        }else if(bookedTime.start() - length >= TimeRange.START_OF_DAY) {
            availTimes.add(bookedTime.fromStartEnd(TimeRange.START_OF_DAY, bookedTime.start(), false)); 
        }
        prevBookedTime = bookedTime;
    }

    Collections.sort(availTimes, TimeRange.ORDER_BY_START);
    return availTimes;
  }
}
