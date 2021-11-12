package com.stuintech.sonicdevices.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DeferredList<T> implements List<T> {
    private final T first;
    private final List<T> second;
    
    public DeferredList(T first, List<T> second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public int size() {
        return second.size() + 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return first == o || second.contains(o);
    }
    
    public ArrayList<T> fullList() {
        ArrayList<T> temp = new ArrayList(second);
        temp.add(0, first);
        return temp;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return fullList().iterator();
    }
    
    @Override
    public Object @NotNull [] toArray() {
        return fullList().toArray();
    }
    
    @Override
    public <T> T @NotNull [] toArray(@NotNull T[] a) {
        return fullList().toArray(a);
    }

    @Override
    public boolean add(T identifier) {
        return second.add(identifier);
    }

    @Override
    public boolean remove(Object o) {
        return second.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return second.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return second.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        return second.addAll(index - 1, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return second.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return second.retainAll(c);
    }

    @Override
    public void clear() {
        second.clear();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DeferredList && ((DeferredList<?>) o).first == first && ((DeferredList<?>) o).second == second;
    }

    @Override
    public int hashCode() {
        return second.hashCode();
    }

    @Override
    public T get(int index) {
        if(index == 0)
            return first;
        return second.get(index - 1);
    }

    @Override
    public T set(int index, T element) {
        return second.set(index - 1, element);
    }

    @Override
    public void add(int index, T element) {
        second.add(index - 1, element);

    }

    @Override
    public T remove(int index) {
        return second.remove(index - 1);
    }

    @Override
    public int indexOf(Object o) {
        if(o == first)
            return 0;
        int i = second.indexOf(o);
        if(i == -1)
            return -1;
        return i + 1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int i = second.lastIndexOf(o);
        if(o == first && i == -1)
            return 0;
        if(i == -1)
            return -1;
        return i + 1;
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return fullList().listIterator();
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return fullList().listIterator(index);
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return fullList().subList(fromIndex, toIndex);
    }
}
