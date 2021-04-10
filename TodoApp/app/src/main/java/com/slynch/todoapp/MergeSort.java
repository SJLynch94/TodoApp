package com.slynch.todoapp;

public class MergeSort {
    // Merge the sub-arrays that are fro left to mid and mid to right
    public void merge(TodoItem[] data, int left, int mid, int right) {
        // Calculate sub-array sizes
        int leftArraySize = mid - left + 1;
        int rightArraySize = right - mid;

        // Create temp arrays to merge with sub-array sizes
        TodoItem[] leftArray = new TodoItem[leftArraySize];
        TodoItem[] rightArray = new TodoItem[rightArraySize];

        // Copy data from the main array
        for(int i = 0; i < leftArraySize; ++i) {
            leftArray[i] = data[left + i];
        }
        for(int i = 0; i < rightArraySize; ++i) {
            rightArray[i] = data[mid + 1 + i];
        }

        // Create initial indexes of the sub-arrays and the merging index
        int i = 0, j = 0;
        int mergeIndex = left;

        // Loop over the array
        while (i < leftArraySize && j < rightArraySize) {
            // Check the completion data value
            if(leftArray[i].getCompletionDate() <= rightArray[j].getCompletionDate()) {
                data[mergeIndex] = leftArray[i];
                ++i;
            } else {
                data[mergeIndex] = rightArray[j];
                ++j;
            }
            ++mergeIndex;
        }

        // Add any remaining back into the main array
        while (i < leftArraySize) {
            data[mergeIndex] = leftArray[i];
            ++i;
            ++mergeIndex;
        }
        while (j < rightArraySize) {
            data[mergeIndex] = rightArray[j];
            ++j;
            ++mergeIndex;
        }
    }

    // Sorting function to sort the array with left and right
    public void sort(TodoItem[] data, int left, int right) {
        if (left < right) {
            // Calculate mid point of the array
            int mid = (left + right) / 2;
            // Sort the left and right side of the array
            sort(data, left, mid);
            sort(data, mid + 1, right);
            // Merge the sorted arrays of left and right
            merge(data, left, mid, right);
        }
    }
}