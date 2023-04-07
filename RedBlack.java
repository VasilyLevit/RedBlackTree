package Tree;

public class RedBlack {
    // корень
    private Node root;
    /**
     * @param value значение хранимое в узле
     * @return
     */
    public boolean add(int value) {
        if (root != null) {
            boolean result = addNode(root, value);
            root = rebalance(root);
            root.color = Color.BLACK;
            return result;
        } else {
            root = new Node();
            root.color = Color.BLACK;
            root.value = value;
            return true;
        }
    }

    /**
     * Добавление нового узла
     * @param node
     * @param value значение ноды
     * @return boolean - бинарном дереве все значения дожны быть уникальны
     */
    private boolean addNode(Node node, int value) {
        // проверка на существование с указанным значением
        if (node.value == value) {
            return false;
        } else {
            if (node.value > value) {
                if (node.leftChild != null) {
                    /* поиск рекурсивно по левому ребенку в глубину с проверкой - можно ли создать узел там*/
                    boolean result = addNode(node.leftChild, value);
                    /* после успешного добавления ноды, возвращаеясь из рекурсивного вызова
                     * проверяем, нужно ли делать балансировку */
                    node.leftChild = rebalance(node.leftChild);
                    return result;
                } else {
                    /* если левой ноды не существует, считае поиск подходящего, для подстановки value
                    места удачным, генерируем ноду и присваиваем ей красный цвет (все ноды при создании
                    получают красный цвет*/
                    node.leftChild = new Node();
                    node.leftChild.color = Color.RED;
                    node.leftChild.value = value;
                    return true;
                }
        } else {
                if (node.rightChild != null) {
                    /* поиск рекурсвно для левого ребенка. Если есть, поиск рекурсивно в глубину */
                    boolean result = addNode(node.rightChild, value);
                    node.rightChild = rebalance(node.rightChild);
                    return result;
                } else {
                    /* если правого ребенка нет, то генерируем значение */
                    node.rightChild = new Node();
                    node.rightChild.color = Color.RED;
                    node.rightChild.value = value;
                    return true;
                }
            }
        }
    }

    /**
     * Медот ребалансирвки
     * @param node
     * @return
     */
    private Node rebalance(Node node) {
        Node result = node;
        boolean needRebalance;
        do {
            needRebalance = false;
            if (result.rightChild != null && result.rightChild.color == Color.RED &&
                    (result.leftChild == null || result.leftChild.color == Color.BLACK)) {
                needRebalance = true;
                result = rightSwap(result);
            }
            if (result.leftChild != null && result.leftChild.color == Color.RED &&
                    result.leftChild.leftChild != null && result.leftChild.leftChild.color == Color.RED) {
                needRebalance = true;
                result = leftSwap(result);
            }
            if (result.leftChild != null && result.leftChild.color == Color.RED &&
                    result.rightChild != null && result.rightChild.color == Color.RED) {
                needRebalance = true;
                colorSwap(result);
            }
        }
        while (needRebalance);
        return result;
    }

    /**
     * Правосторнний поворот
     * @param node
     * @return
     */
    private Node rightSwap(Node node) {
        Node righrChild = node.rightChild;
        Node betweenChild = righrChild.leftChild;
        righrChild.leftChild = node;
        node.rightChild = betweenChild;
        righrChild.color = node.color;
        node.color = Color.RED;
        return righrChild;
    }

    /**
     * Левосторонний поворот (левосторонняя нода - карсная) 
     * 
     * @param node
     * @return
     */
    private Node leftSwap(Node node) {
        /* выделяем левого ребека как одтельную переменную (для удобства) */
        Node leftChild = node.leftChild;
        /* промежуточный ребенок - элемент, который будет менять своего родителя - т.е. родители
         * будут меняться местами относительно родитель-ребенок
        */
        Node betweenChild = leftChild.rightChild;
        /* вместо правого ребенка красной ноды назначаем текущего родителя*/
        leftChild.rightChild = node;
        /* у родителя левым элементом становится промежуточный, значение которого между красной нодой и рутовой*/
        node.leftChild = betweenChild;
        /* левый ребенок получает цвет своего родителя */
        leftChild.color = node.color;
        /* корень, который стал ниже становится красным */
        node.color = Color.RED;
        return leftChild;
    }

    /**
     * Смена цвета (когда у ноды 2а красных ребёнка) - дети становятся черными, а текущая нода красная
     * @param node
     */
    private void colorSwap(Node node) {
        node.rightChild.color = Color.BLACK;
        node.leftChild.color = Color.BLACK;
        node.color = Color.RED;
    }


    private class Node {
        private int value;
        private Color color;
        private Node leftChild;
        private Node rightChild;

        @Override
        public String toString(){
            return "Node{" +
                    "value=" + value +
                    ", color=" + color +
                    "}";
        }
    }

    private enum Color {
        RED, BLACK
    }
}