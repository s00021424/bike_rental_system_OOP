package com.epicode;

public interface BikeCollection<T> {
    Iterator<T> createIterator();
    int getSize();
    T getElementAt(int index);
}
