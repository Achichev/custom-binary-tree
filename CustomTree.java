package com.javarush.task.task20.task2028;

import java.io.Serializable;
import java.util.*;

/*
Построй дерево(1)
*/

public class CustomTree extends AbstractList<String> implements Cloneable, Serializable {
    Entry<String> root;
    List<Entry<String>> listOfElements = new LinkedList<>();
    List<Entry<String>> listForRemove = new ArrayList<>();

    public CustomTree() {
        root = new Entry<>("0");
        listOfElements.add(root);
    }

    @Override
    public String get(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return listOfElements.size() - 1;
    }

    @Override
    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(String s) {
        boolean elementAdded = false;
        if (listOfElements.size() > 0) {
            for (Entry<String> entry: listOfElements) {
                if (entry.availableToAddLeftChildren) {
                    entry.leftChild = new Entry<>(s);
                    entry.leftChild.parent = entry;
                    elementAdded = listOfElements.add(entry.leftChild);
                    entry.availableToAddLeftChildren = false;
                    break;
                } else if (entry.availableToAddRightChildren) {
                    entry.rightChild = new Entry<>(s);
                    entry.rightChild.parent = entry;
                    elementAdded = listOfElements.add(entry.rightChild);
                    entry.availableToAddRightChildren = false;
                    break;
                }
            }
        }
        return elementAdded;
    }

    public String getParent(String s) {
        String parent = null;
        if (listOfElements.size() > 1) {
            for (Entry<String> entry: listOfElements) {
                if (entry.elementName.equals(s)) {
                    parent = entry.parent.elementName;
                }
            }
        }
        return parent;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof String)) throw new UnsupportedOperationException();
        boolean elementRemoved = false;
        for (Entry<String> entry: listOfElements) {
            if (!entry.elementName.equals(o.toString())) {
                continue;
            }
            listForRemove.add(entry);
            searchElements(entry);
        }
        for (Entry<String> entry: listForRemove) {
            elementRemoved = listOfElements.remove(entry);
        }
        listForRemove.clear();
        return elementRemoved;
    }

    public void searchElements(Entry<String> entry) {
        for (Entry<String> element: listOfElements) {
            if (element.equals(entry)) {
                if (!element.availableToAddLeftChildren) {
                    listForRemove.add(element.leftChild);
                }
                if (!element.availableToAddRightChildren) {
                    listForRemove.add(element.rightChild);
                }
                if (isLeftChild(element.parent, element)) {
                    element.parent.availableToAddLeftChildren = true;
                } else element.parent.availableToAddRightChildren = true;

                searchElements(element.leftChild);
                searchElements(element.rightChild);
            }
        }
    }

    public boolean isLeftChild(Entry<String> parent, Entry<String> child) {
        return parent.leftChild.elementName.equals(child.elementName);
    }

    public boolean isRightChild(Entry<String> parent, Entry<String> child) {
        return parent.rightChild.elementName.equals(child.elementName);
    }

    static class Entry<T> implements Serializable {
        String elementName;
        boolean availableToAddLeftChildren, availableToAddRightChildren;
        Entry<T> parent, leftChild, rightChild;

        public Entry(String elementName) {
            this.elementName = elementName;
            availableToAddLeftChildren = true;
            availableToAddRightChildren = true;
        }

        public boolean isAvailableToAddChildren() {
            return availableToAddLeftChildren || availableToAddRightChildren;
        }
    }
}
