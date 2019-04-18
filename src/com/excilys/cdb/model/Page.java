package com.excilys.cdb.model;

import java.util.List;

import com.excilys.cdb.exception.SliceNotFoundException;

public class Page<T> {

	List<T> fullList;
	int size;
	int sliceIndex = 0;
	
	public Page(List<T> pList, int pSize) {
		this.fullList = pList;
		this.size = pSize;
	}
	
	/**
	 * Get the current Slice of the page
	 * @return a list of data
	 */
	public List<T> getSlice() {
		return fullList.subList(sliceIndex*size, Math.min(fullList.size(), (sliceIndex+1)*size));
	}
	
	/**
	 * Change the slice to the next one
	 * @return the page
	 * @throws SliceNotFoundException
	 */
	public Page<T> nextSlice() throws SliceNotFoundException {
		if(sliceIndex >= fullList.size()/size) {
			throw new SliceNotFoundException("There is no next page !");
		}
		sliceIndex++;
		return this;
	}
	
	/**
	 * Change the slice to the previous one
	 * @return the page
	 * @throws SliceNotFoundException
	 */
	public Page<T> previousSlice() throws SliceNotFoundException {
		if(sliceIndex <= 0) {
			throw new SliceNotFoundException("There is no previous page !");
		}
		sliceIndex--;
		return this;
	}
	
	/**
	 * Set the slice to an index
	 * @param pSliceIndex 
	 * @return the page
	 * @throws SliceNotFoundException
	 */
	public Page<T> setSlice(int pSliceIndex) throws SliceNotFoundException{
		if(pSliceIndex<0 || pSliceIndex > fullList.size()/size) {
			throw new SliceNotFoundException("This page doesn't exist !");
		}
		this.sliceIndex = pSliceIndex;
		return this;
	}
	
	/**
	 * Get the index of the last slice of the page
	 * @return the index of the last slice
	 */
	public int lastSlice(){
		return fullList.size()/size;
	}
	
}
