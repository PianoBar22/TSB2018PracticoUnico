package datos;

import java.io.Serializable;
import java.util.*;

public class TSB_OAHashtable<K,V> implements Map<K,V>, Cloneable, Serializable {
    //************************ Constantes (privadas o públicas).

    // el tamaño máximo que podrá tener el arreglo de soprte...
    private final static int MAX_SIZE = Integer.MAX_VALUE;


    //************************ Atributos privados (estructurales).

    // la tabla hash: el arreglo que contiene los datos...
    private Map.Entry<K, V> table[];

    // el tamaño inicial de la tabla (tamaño con el que fue creada)...
    private int initial_capacity;

    // la cantidad de objetos que contiene la tabla en TODAS sus listas...
    private int count;

    // el factor de carga para calcular si hace falta un rehashing...
    private float load_factor;

    private Map.Entry<K, V> tumba;

    //************************ Atributos privados (para gestionar las vistas).

    /*
     * (Tal cual están definidos en la clase java.util.Hashtable)
     * Cada uno de estos campos se inicializa para contener una instancia de la
     * vista que sea más apropiada, la primera vez que esa vista es requerida.
     * La vista son objetos stateless (no se requiere que almacenen datos, sino
     * que sólo soportan operaciones), y por lo tanto no es necesario crear más
     * de una de cada una.
     */
    private transient Set<K> keySet = null;
    private transient Set<Map.Entry<K,V>> entrySet = null;
    private transient Collection<V> values = null;


    //************************ Atributos protegidos (control de iteración).

    // conteo de operaciones de cambio de tamaño (fail-fast iterator).
    protected transient int modCount;


    //************************ Constructores.

    /**
     * Crea una tabla vacía, con la capacidad inicial igual a 11 y con factor
     * de carga igual a 0.8f.
     */
    public TSB_OAHashtable()
    {
        this(5, 0.8f);
    }

    /**
     * Crea una tabla vacía, con la capacidad inicial indicada y con factor
     * de carga igual a 0.8f.
     * @param initial_capacity la capacidad inicial de la tabla.
     */
    public TSB_OAHashtable(int initial_capacity)
    {
        this(initial_capacity, 0.8f);
    }

    /**
     * Crea una tabla vacía, con la capacidad inicial indicada y con el factor
     * de carga indicado. Si la capacidad inicial indicada por initial_capacity
     * es menor o igual a 0, la tabla será creada de tamaño 11. Si el factor de
     * carga indicado es negativo o cero, se ajustará a 0.8f.
     * @param initial_capacity la capacidad inicial de la tabla.
     * @param load_factor el factor de carga de la tabla.
     */
    public TSB_OAHashtable(int initial_capacity, float load_factor)
    {
        tumba = new Entry<>();

        if(load_factor <= 0) { load_factor = 0.8f; }
        if(initial_capacity <= 0) { initial_capacity = 11; }
        else
        {
            if(initial_capacity > this.MAX_SIZE)
            {
                initial_capacity = this.MAX_SIZE;
            }
        }

        this.table = new Map.Entry[initial_capacity];
        for(int i=0; i<table.length; i++)
        {
            table[i] = null;
        }

        this.initial_capacity = initial_capacity;
        this.load_factor = load_factor;
        this.count = 0;
        this.modCount = 0;
    }

    /**
     * Crea una tabla a partir del contenido del Map especificado.
     * @param t el Map a partir del cual se creará la tabla.
     */
    public TSB_OAHashtable(Map<? extends K,? extends V> t)
    {
        this(11, 0.8f);
        this.putAll(t);
    }


    @Override
    public int size() {
        return this.count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return (this.get((K)key) != null);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.contains(value);
    }

    private boolean contains(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        int hash = this.search_by_key((K)key);

        if (hash >= 0)
            return this.table[hash].getValue();
        else
            return null;
    }

    @Override
    public V put(K key, V value) {
        int hash = this.h(key);
        int initialHash = -1;
        int indexOfTumba = -1;

        V old = null;
        while (hash != initialHash
                && ((table[hash] != null || table[hash] == this.tumba)
                && table[hash].getKey() != key)) {

            if (initialHash == -1)
                initialHash = hash;

            if (table[hash] == this.tumba)
                indexOfTumba = hash;

            hash = this.h(hash + 1);
        }

        if ((table[hash] == null || hash == initialHash) && indexOfTumba != -1)
        {
            table[indexOfTumba] = new Entry<>(key, value);
            this.count++;
        }

        else if (initialHash != hash)
            if (table[hash] != this.tumba && table[hash] != null && table[hash].getKey() == key)
            {
                old = table[hash].getValue();
                table[hash].setValue(value);
            }
            else
            {
                if(this.averageLength() >= this.load_factor * 10) this.rehash();
                table[hash] = new Entry<>(key, value);
                this.count++;
            }

        return old;
    }

    private int averageLength() {
        return this.count / this.table.length;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {
        this.table = new Map.Entry[this.initial_capacity];

        for(int i = 0; i < this.table.length; i++)
        {
            this.table[i] = null;
        }

        this.count = 0;
        this.modCount++;
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if(entrySet == null)
        {
            // entrySet = Collections.synchronizedSet(new EntrySet());
            entrySet = new EntrySet();
        }
        return entrySet;
    }

    @Override
    public String toString() {
        Iterator<Map.Entry<K, V>> it = this.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        while (it.hasNext())
        {
            sb.append(it.next().toString());
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    //************************ Métodos privados.

    private int search_by_key(K key){
        int hash = this.h(key);
        int initialHash = -1;

        while (hash != initialHash
                && ((table[hash]== tumba || table[hash] != null)
                && table[hash].getKey() != key)) {

            if (initialHash == -1)
                initialHash = hash;

            hash = this.h(hash + 1);
        }

        if (table[hash] == null || hash == initialHash)
            return -1;
        else
            return hash;
    }

    protected void rehash()
    {
        int old_length = this.table.length;

        // nuevo tamaño: doble del anterior, más uno para llevarlo a impar...
        int new_length = old_length * 2 + 1;

        // no permitir que la tabla tenga un tamaño mayor al límite máximo...
        // ... para evitar overflow y/o desborde de índices...
        if(new_length > TSB_OAHashtable.MAX_SIZE)
        {
            new_length = TSB_OAHashtable.MAX_SIZE;
        }

        // crear el nuevo arreglo con new_length listas vacías...
        Map.Entry<K, V> temp[] = new Map.Entry[new_length];
        for(int j = 0; j < temp.length; j++) { temp[j] = null; }

        // notificación fail-fast iterator... la tabla cambió su estructura...
        this.modCount++;

        // recorrer el viejo arreglo y redistribuir los objetos que tenia...
        Iterator<Map.Entry<K, V>> it = this.entrySet().iterator();

        while(it.hasNext())
        {
            // obtener un objeto de la vieja lista...
            Map.Entry<K, V> x = it.next();

            // obtener su nuevo valor de dispersión para el nuevo arreglo...
            K key = x.getKey();
            int y = this.h(key, temp.length);

            // insertarlo en el nuevo arreglo, en la lista numero "y"...
            while (temp[y] != null)
            {
                y = this.h(y + 1, temp.length);
            }
            temp[y] = x;
        }

        // cambiar la referencia table para que apunte a temp...
        this.table = temp;
    }

    /*
     * Función hash. Toma una clave entera k y calcula y retorna un índice
     * válido para esa clave para entrar en la tabla.
     */
    private int h(int k)
    {
        return h(k, this.table.length);
    }

    /*
     * Función hash. Toma un objeto key que representa una clave y calcula y
     * retorna un índice válido para esa clave para entrar en la tabla.
     */
    private int h(K key)
    {
        return h(key.hashCode(), this.table.length);
    }

    /*
     * Función hash. Toma un objeto key que representa una clave y un tamaño de
     * tabla t, y calcula y retorna un índice válido para esa clave dedo ese
     * tamaño.
     */
    private int h(K key, int t)
    {
        return h(key.hashCode(), t);
    }

    /*
     * Función hash. Toma una clave entera k y un tamaño de tabla t, y calcula y
     * retorna un índice válido para esa clave dado ese tamaño.
     */
    private int h(int k, int t)
    {
        if(k < 0) k *= -1;
        return k % t;
    }

    //************************ Clases Internas.

    private class Entry<K, V> implements Map.Entry<K, V>, Serializable{

        private K key;
        private V value;

        public Entry(K key, V value)
        {
            if(key == null || value == null)
            {
                throw new IllegalArgumentException("Entry(): parámetro null...");
            }
            this.key = key;
            this.value = value;
        }

        public Entry() {

        }

        @Override
        public K getKey()
        {
            return key;
        }

        @Override
        public V getValue()
        {
            return value;
        }

        @Override
        public V setValue(V value)
        {
            if(value == null)
            {
                throw new IllegalArgumentException("setValue(): parámetro null...");
            }

            V old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public int hashCode()
        {
            int hash = 7;
            hash = 61 * hash + Objects.hashCode(this.key);
            hash = 61 * hash + Objects.hashCode(this.value);
            return hash;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj) { return true; }
            if (obj == null) { return false; }
            if (this.getClass() != obj.getClass()) { return false; }

            final TSB_OAHashtable.Entry other = (TSB_OAHashtable.Entry) obj;
            if (!Objects.equals(this.key, other.key)) { return false; }
            if (!Objects.equals(this.value, other.value)) { return false; }
            return true;
        }

        @Override
        public String toString()
        {
            return "(" + key.toString() + ", " + value.toString() + ")";
        }
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>>{

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntrySetIterator();
        }

        @Override
        public int size() {
            return TSB_OAHashtable.this.count;
        }

        private class EntrySetIterator implements Iterator<Map.Entry<K, V>>
        {
            private int current_index;
            private int previous_index;

            public EntrySetIterator() {
                current_index = -1;
                previous_index = -1;
            }

            /**
             * Indica si queda algun objeto en el recorrido del iterador.          *
             * @return true si queda algun objeto en el recorrido - false si no
             * quedan objetos.
             */
            @Override
            public boolean hasNext() {
                // variable auxiliar t para simplificar accesos...
                Map.Entry<K, V> t[] = TSB_OAHashtable.this.table;

                if(TSB_OAHashtable.this.isEmpty()) { return false; }
                if(current_index >= t.length) { return false; }

                int next_bucket = current_index + 1;

                while(next_bucket < t.length && t[next_bucket] == null && t[next_bucket] != tumba)
                {
                    next_bucket++;
                }

                if(next_bucket >= t.length) { return false; }

                return true;
            }

            @Override
            public Map.Entry<K, V> next() {
                if(!hasNext())
                {
                    throw new NoSuchElementException("next(): no existe el elemento pedido...");
                }

                // variable auxiliar t para simplificar accesos...
                Map.Entry<K, V> t[] = TSB_OAHashtable.this.table;

                previous_index = current_index;

                // buscar el siguiente indice no vacío, que DEBE existir, ya
                // que se hasNext() retornó true...
                current_index++;
                while(t[current_index] == null || t[current_index] == tumba)
                {
                    current_index++;
                }

                // y retornar el elemento alcanzado...
                return t[current_index];
            }
        }
    }
}
