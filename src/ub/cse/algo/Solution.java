package ub.cse.algo;

import java.util.*;

/**
 * For use in CSE 331 HW1.
 * This is the class you will be editing and turning in. It will be timed against our implementation
 * NOTE that if you declare this file to be in a package, it will not compile in Autolab
 */

public class Solution {
	private int _nHospital;
	private int _nStudent;

    // The following represent the preference list of hospitals and students.
    // The KEY represents the integer representation of a given hospital or student.
    // The VALUE is a list, from most preferred to least.
    // For hospital, first element of the list is number of available slots
	private HashMap<Integer, ArrayList<Integer>> _hospitalList;
	private HashMap<Integer, ArrayList<Integer>> _studentList;
    
    
    /**
     * The constructor simply sets up the necessary data structures.
     * The grader for the homework will first call this class and pass the necessary variables.
     * There is no need to edit this constructor.
     * @param m Number of hospitals
     * @param n Number of students
     * @param A map linking each hospital with its preference list
     * @param A map linking each student with their preference list
     * @return
     */
	public Solution(int m, int n, HashMap<Integer, ArrayList<Integer>> hospitalList, HashMap<Integer, ArrayList<Integer>> studentList) {
		_nHospital = m;
		_nStudent = n;
		_hospitalList = hospitalList;
		_studentList = studentList;
	}
    
    /**
     * This method must be filled in by you. You may add other methods and subclasses as you see fit,
     * but they must remain within the HW1_Student_Solution class.
     * @return Your stable matches
     */
	public ArrayList<Match> getMatches() {

		ArrayList<Match> matchList = new ArrayList<>();
		ArrayList<Integer> freeList = new ArrayList<>(_studentList.keySet());  // unmatched students
		HashMap<Integer, Match> matchMap = new HashMap<>();  // K: students V: (h,s)
		Queue<Integer> slotIdQueue = new LinkedList<>();  // slots in hospitals

		for (Integer hospital : _hospitalList.keySet()) {
			Integer slots = _hospitalList.get(hospital).get(0);
			for (int i = 0; i < slots; i++) {
				slotIdQueue.add(hospital);
			}
		}

		while (!slotIdQueue.isEmpty()) {

			Integer hospital = slotIdQueue.remove();  // hospital's ID
			ArrayList<Integer> hospitalPrefList = _hospitalList.get(hospital);  // the preference list of the hospital
			int slot = 1;  // the number of slots available

			/*
			  the hospital will match the students in the order of its preferred list
			*/
			for (int i = 1; i <= _nStudent; i++) {

				Integer student = hospitalPrefList.get(i);  // the student in the list

				// All slots are unavailable
				if (slot == 0) {
					break;

					//There	is at least one slot available in the hospital
				} else if (slot > 0) {

					// The student is unmatched, get in immediately
					if (freeList.contains(student)) {
						Match assignment = new Match(hospital, student);
						freeList.remove(student);
						matchMap.put(student, assignment);
						matchList.add(assignment);
						slot--;
					}

					// the student is matched, check the current partner and compare
					if (matchMap.containsKey(student)) {
						ArrayList<Integer> studentPrefList = _studentList.get(student);  // the student's preference list
						Integer otherHospital = matchMap.get(student).hospital; // the current partner
							// the hospital is preferred
							if (studentPrefList.indexOf(hospital) < studentPrefList.indexOf(otherHospital)) {
								Match assignment = new Match(hospital, student);
								matchList.remove(matchMap.get(student));
								slot--;

								slotIdQueue.add(otherHospital); // A slot is released from the previous hospital
								matchMap.put(student, assignment);
								matchList.add(assignment);
							}
					}
				}
			}
		}
		return matchList;
	}
}