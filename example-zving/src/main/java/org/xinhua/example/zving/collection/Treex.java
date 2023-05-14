package org.xinhua.example.zving.collection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Treex<T> implements Iterable<Treex.TreeNode<T>>, Serializable {
    private static final long serialVersionUID = 1L;

    private TreeNode<T> root = new TreeNode<T>();

    /**
     * 获取根节点对象
     */
    public TreeNode<T> getRoot() {
        return root;
    }

    /**
     * 根据节点数据获取节点对象
     */
    public TreeNode<T> getNode(T data) {// NO_UCD
        TreeIterator<T> ti = iterator();
        while (ti.hasNext()) {
            TreeNode<T> tn = ti.next();
            if (tn.getData().equals(data)) {
                return tn;
            }
        }
        return null;
    }

    /**
     * 遍历器，遍历整个树
     */
    @Override
    public TreeIterator<T> iterator() {
        return new TreeIterator<T>(root);
    }

    /**
     * 以node为起始节点开始遍历
     */
    public static <T> TreeIterator<T> iterator(TreeNode<T> node) {// NO_UCD
        return new TreeIterator<T>(node);
    }

    /**
     * 输出整个树形结构为字符串
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return toString(Formatter.DefaultFormatter);
    }

    /**
     * 以指定的格式输出整个树形结构
     */
    public String toString(Formatter f) {
        StringBuilder sb = new StringBuilder();
        TreeIterator<T> ti = this.iterator();
        while (ti.hasNext()) {
            TreeNode<T> tn = ti.nextNode();
            TreeNode<T> p = tn.getParent();
            String str = "";
            while (p != null && !p.isRoot()) {
                if (p.isLast()) {
                    str = "  " + str;
                } else {
                    str = "│ " + str;
                }
                p = p.getParent();
            }
            sb.append(str);
            if (!tn.isRoot()) {
                if (tn.isLast()) {
                    sb.append("└─");
                } else {
                    sb.append("├─");
                }
            }
            sb.append(f.format(tn.getData()));
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * 获取所有节点组成的数组
     */
    public ArrayList<TreeNode<T>> toArray() {// NO_UCD
        TreeIterator<T> ti = new TreeIterator<T>(root);
        ArrayList<TreeNode<T>> arr = new ArrayList<TreeNode<T>>();
        while (ti.hasNext()) {
            arr.add(ti.next());
        }
        return arr;
    }

    /**
     * 以某一节点为父节点遍历该节点用其所有子节点
     */
    public static class TreeIterator<T> implements Iterator<TreeNode<T>>, Iterable<TreeNode<T>> {
        private TreeNode<T> last;

        private TreeNode<T> next;

        private TreeNode<T> start;

        /**
         * 以node为父节点，构造一个遍历器
         */
        TreeIterator(TreeNode<T> node) {
            start = next = node;
        }

        /**
         * 是否还有下一个节点
         *
         * @see Iterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            if (next == null) {
                return false;
            }
            if (next == start && start.getChildren().size() == 0) {
                return false;
            }
            if (next != start && next.getLevel() == start.getLevel()) {
                return false;
            }
            return true;
        }

        /**
         * 获取下一个节点
         *
         * @see Iterator#next()
         */
        @Override
        public TreeNode<T> next() {
            if (next == null) {
                throw new NoSuchElementException();
            }
            last = next;
            if (next.hasChild()) {
                next = next.getChildren().get(0);
            } else {
                while (next.getNextSibling() == null) {
                    if (next.parent.isRoot()) {
                        next = null;
                        return last;
                    } else {
                        next = next.parent;
                    }
                }
                next = next.getNextSibling();
            }
            return last;
        }

        public TreeNode<T> nextNode() {
            return next();
        }

        public TreeNode<T> currentNode() {// NO_UCD
            return next;
        }

        /**
         * 删除当前节点
         *
         * @see Iterator#remove()
         */
        @Override
        public void remove() {
            if (last == null) {
                throw new IllegalStateException();
            }
            last.parent.getChildren().remove(last);
            last = null;
        }

        @Override
        public Iterator<TreeNode<T>> iterator() {
            return this;
        }
    }

    /**
     * 树形结构中的一个节点
     *
     * @author 王育春
     * @date 2009-11-16
     * @email wangyc@zving.com
     */
    public static class TreeNode<T> implements Serializable {
        private static final long serialVersionUID = 1L;

        private int level;

        private T data;

        private TreeNodeList<T> children = new TreeNodeList<T>();

        private TreeNode<T> parent;

        private int pos;// 在上级元素中的位置

        /**
         * 构造函数
         */
        public TreeNode() {
        }

        /**
         * 为本节点添加一个子节点，并为子节点指定节点数据
         */
        public TreeNode<T> addChild(T data) {
            TreeNode<T> tn = new TreeNode<T>();
            tn.data = data;
            return addChildNode(tn);
        }

        public TreeNode<T> addChildNode(TreeNode<T> tn) {
            tn.level = level + 1;
            tn.parent = this;
            tn.pos = children.size();
            children.add(tn);
            return tn;
        }

        /**
         * 根据节点数据删除一个子节点
         */
        public void removeChild(T data) {
            for (int i = 0; i < children.size(); i++) {
                TreeNode<T> child = children.get(i);
                if (child.getData() == null) {
                    if (data == null) {
                        children.remove(i);
                        break;
                    } else {
                        continue;
                    }
                }
                if (child.getData().equals(data)) {
                    children.remove(i);
                    break;
                }
            }
        }

        /**
         * 根据节点数据获得节点本身
         */
        public TreeNode<T> getChild(T data) {// NO_UCD
            for (int i = 0; i < children.size(); i++) {
                TreeNode<T> child = children.get(i);
                if (child.getData() == null) {
                    if (data == null) {
                        return child;
                    } else {
                        continue;
                    }
                }
                if (child.getData().equals(data)) {
                    return child;
                }
            }
            return null;
        }

        /**
         * 上一个节点，没有上一个节点则返回null
         */
        public TreeNode<T> getPreviousSibling() {
            if (pos == 0) {
                return null;
            }
            return parent.getChildren().get(pos - 1);
        }

        /**
         * 下一个节点,没有下一个节点则返回null
         */
        public TreeNode<T> getNextSibling() {
            if (parent == null || pos == parent.getChildren().size() - 1) {
                return null;
            }
            return parent.getChildren().get(pos + 1);
        }

        /**
         * 节点在树形结构中的级别，根节点为0
         */
        public int getLevel() {
            return level;
        }

        /**
         * 是否是根节点
         */
        public boolean isRoot() {
            return parent == null;
        }

        /**
         * 是否是最后一个节点
         */
        public boolean isLast() {
            if (parent != null && pos != parent.getChildren().size() - 1) {
                return false;
            }
            return true;
        }

        /**
         * 是否含有子节点
         */
        public boolean hasChild() {
            return children.size() != 0;
        }

        /**
         * 获取上一级节点对象
         */
        public TreeNode<T> getParent() {
            return parent;
        }

        /**
         * 当前节点在父节点的所有子节点中是第几个
         */
        public int getPosition() {
            return pos;
        }

        /**
         * 获取所有子节点列表
         */
        public TreeNodeList<T> getChildren() {
            return children;
        }

        /**
         * 获取节点数据
         */
        public T getData() {
            return data;
        }

        /**
         * 设置节点数据
         */
        public void setData(T data) {
            this.data = data;
        }
    }

    /**
     * 节点列表类
     *
     * @author 王育春
     * @date 2009-11-16
     * @email wangyc@zving.com
     */
    public static class TreeNodeList<T> extends ArrayList<TreeNode<T>> {
        private static final long serialVersionUID = 1L;

        /**
         * 删除一个子节点
         */
        public TreeNode<T> remove(TreeNode<T> node) {
            int pos = node.getPosition();
            for (int i = pos + 1; i < size(); i++) {
                TreeNode<T> tn = get(i);
                tn.pos--;
            }
            super.remove(node);
            return node;
        }

        /**
         * 获取最后一个子节点
         */
        public TreeNode<T> last() {// NO_UCD
            int size = size();
            if (size == 0) {
                return null;
            }
            return get(size - 1);
        }
    }

}
