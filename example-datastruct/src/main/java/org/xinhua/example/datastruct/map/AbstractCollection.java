package org.xinhua.example.datastruct.map;

import java.util.function.Consumer;

/**
 * @Author: lilong
 * @createDate: 2023/4/27 0:39
 * @Description: 抽象Collection
 * @Version: 1.0
 */
public abstract class AbstractCollection<E> implements Collection<E> {

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(E e) {
        Iterator<E> it = iterator();
        if (e == null) {
            while (it.hasNext()) {
                if (it.next() == null) {
                    return true;
                }
            }
        } else {
            while (it.hasNext()) {
                if (e.equals(it.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsAll(Collection<E> c) {
        Iterator<E> it = c.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(Collection<E> c) {
        boolean modified = false;
        Iterator<E> it = c.iterator();
        while (it.hasNext()) {
            if (add(it.next())) {
                modified = true;
            }
        }
        return modified;
    }

    public boolean removeAll(Collection<E> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    public void clear() {
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            action.accept(it.next());
        }
    }
}
