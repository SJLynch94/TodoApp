package com.slynch.todoapp;

import java.util.ArrayList;

public class MergeSortArrayList {

    private ArrayList<TodoItem> inputArray;

    public ArrayList<TodoItem> getSortedArray() {
        return inputArray;
    }

    public MergeSortArrayList(ArrayList<TodoItem> inputArray){
        this.inputArray = inputArray;
    }

    public MergeSortArrayList() {}

    public void sortGivenArray(){
        divide(0, this.inputArray.size() - 1);
    }

    public void setInputArray(ArrayList<TodoItem> inputArray) {
        this.inputArray = inputArray;
    }

    public void divide(int startIndex, int endIndex) {
        // Divide till you breakdown your list to single element
        if(startIndex < endIndex && (endIndex - startIndex) >= 1) {
            //int mid = (endIndex + startIndex) / 2;
            int mid = (startIndex + endIndex) / 2;
            divide(startIndex, mid);
            divide(mid + 1, endIndex);
            // Merging Sorted array produce above into one sorted array
            merge(startIndex, mid, endIndex);
        }
    }

    public void merge(int startIndex, int midIndex, int endIndex) {

        int leftArraySize = midIndex - startIndex + 1;
        int rightArraySize = endIndex - midIndex;

        // Create temp arrays to merge with sub-array sizes
        TodoItem[] leftArray = new TodoItem[leftArraySize];
        TodoItem[] rightArray = new TodoItem[rightArraySize];

        // Copy data from the main array
        for(int i = 0; i < leftArraySize; ++i) {
            leftArray[i] = inputArray.get(startIndex + i);
        }
        for(int i = 0; i < rightArraySize; ++i) {
            rightArray[i] = inputArray.get(midIndex + 1 + i);
        }

        // Create initial indexes of the sub-arrays and the merging index
        int i = 0, j = 0;
        int mergeIndex = startIndex;

        // Loop over the array
        while (i < leftArraySize && j < rightArraySize) {
            // Check the completion data value
            if(leftArray[i].getCompletionDate() <= rightArray[j].getCompletionDate()) {
                inputArray.set(mergeIndex, leftArray[i]);
                ++i;
            } else {
                inputArray.set(mergeIndex, rightArray[j]);
                ++j;
            }
            ++mergeIndex;
        }

        // Add any remaining back into the main array
        while (i < leftArraySize) {
            inputArray.set(mergeIndex, leftArray[i]);
            ++i;
            ++mergeIndex;
        }
        while (j < rightArraySize) {
            inputArray.set(mergeIndex, rightArray[j]);
            ++j;
            ++mergeIndex;
        }
    }
}
