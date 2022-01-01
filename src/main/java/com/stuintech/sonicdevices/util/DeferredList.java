package com.stuintech.sonicdevices.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DeferredList<T> implements List<T> {
    private final List<T> main;
    private final T last;
    
    public DeferredList(T last, List<T> main) {
        this.main = main;
        this.last = last;
    }
    
    @Override
    public int size() {
        return main.size() + 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return last == o || main.contains(o);
    }
    
    public ArrayList<T> fullList() {
        ArrayList<T> temp = new ArrayList(main);
        temp.add(last);
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
        return main.add(identifier);
    }

    @Override
    public boolean remove(Object o) {
        return main.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return main.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return main.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        return main.addAll(index - 1, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return main.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return main.retainAll(c);
    }

    @Override
    public void clear() {
        main.clear();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof DeferredList && ((DeferredList<?>) o).last == last && ((DeferredList<?>) o).main == main;
    }

    @Override
    public int hashCode() {
        return main.hashCode();
    }

    @Override
    public T get(int index) {
        if(index == main.size())
            return last;
        return main.get(index);
    }

    @Override
    public T set(int index, T element) {
        return main.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        main.add(index, element);

    }

    @Override
    public T remove(int index) {
        return main.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        int i = main.indexOf(o);
        if(i == -1 && o == last)
            return main.size();
        return i;
    }

    @Override
    public int lastIndexOf(Object o) {
        if(o == last)
            return main.size();
        return main.lastIndexOf(o);
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
