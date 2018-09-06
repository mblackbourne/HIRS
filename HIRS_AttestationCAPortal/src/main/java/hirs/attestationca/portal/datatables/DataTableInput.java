package hirs.attestationca.portal.datatables;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a data table input in a jQuery DataTable.
 */
public class DataTableInput {

    private static final int DEFAULT_LENGTH = 10;

    /**
     * Default Constructor.
     */
    public DataTableInput() {
    }

    /**
     * Constructor.
     * @param draw the draw counter
     * @param start the paging start indicator
     * @param length the number of records in current draw
     * @param search the search parameter
     * @param order the orderings
     * @param columns the columns of the input
     */
    public DataTableInput(final Integer draw, final Integer start, final Integer length,
                          final Search search, final List<Order> order,
                          final List<Column> columns) {
        this.draw = draw;
        this.start = start;
        this.length = length;
        this.search = search;
        this.order.addAll(order);
        this.columns.addAll(columns);
    }

    /**
     * Draw counter. This is used by DataTables to ensure that the Ajax returns from server-side
     * processing requests are drawn in sequence by DataTables (Ajax requests are asynchronous and
     * thus can return out of sequence). This is used as part of the draw return parameter (see
     * below).
     */
    @NotNull
    @Min(0)
    private int draw = 1;

    /**
     * Paging first record indicator. This is the start point in the current data set
     * (0 index based - i.e. 0 is the first record).
     */
    @NotNull
    @Min(0)
    private int start = 0;

    /**
     * Number of records that the table can display in the current draw. It is expected that the
     * number of records returned will be equal to this number,
     * unless the server has fewer records to return. Note that this can be -1 to indicate that
     * all records should be returned (although that
     * negates any benefits of server-side processing!)
     */
    @NotNull
    @Min(-1)
    private int length = DEFAULT_LENGTH;

    /**
     * Global search parameter.
     */
    @NotNull
    private Search search = new Search();

    /**
     * Order parameter.
     */
    @NotEmpty
    private List<Order> order = new ArrayList<>();

    /**
     * Per-column search parameter.
     */
    @NotEmpty
    private List<Column> columns = new ArrayList<>();


    /**
     *
     * @return the draw counter
     */
    public int getDraw() {
        return draw;
    }

    /**
     * Sets the draw counter.
     * @param draw the draw counter
     */
    public void setDraw(final int draw) {
        this.draw = draw;
    }

    /**
     * Gets the start indicator.
     * @return the start indicator
     */
    public int getStart() {
        return start;
    }

    /**
     * Gets the start indicator.
     * @param start the start indicator
     */
    public void setStart(final int start) {
        this.start = start;
    }

    /**
     *
     * @return the table length for the current draw
     */
    public int getLength() {
        return length;
    }

    /**
     * Sets the table length for the current draw.
     * @param length the table length for the current draw
     */
    public void setLength(final int length) {
        this.length = length;
    }

    /**
     *
     * @return the Search
     */
    public Search getSearch() {
        return search;
    }

    /**
     * Sets the search.
     * @param search the search
     */
    public void setSearch(final Search search) {
        this.search = search;
    }

    /**
     *
     * @return the orders
     */
    public List<Order> getOrder() {
        return order;
    }

    /**
     * Sets the orders.
     * @param order the orders
     */
    public void setOrder(final List<Order> order) {
        this.order.clear();
        this.order.addAll(order);
    }

    /**
     * Gets the table columns.
     * @return the columns
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * Sets the table columns.
     * @param columns the columns
     */
    public void setColumns(final List<Column> columns) {
        this.columns.clear();
        this.columns.addAll(columns);
    }

    /**
     *
     * @return a {@link Map} of {@link Column} indexed by name
     */
    public Map<String, Column> getColumnsAsMap() {
        Map<String, Column> map = new HashMap<String, Column>();
        for (Column column : columns) {
            map.put(column.getData(), column);
        }
        return map;
    }

    /**
     * Find a column by its name.
     *
     * @param columnName the name of the column
     * @return the given Column, or <code>null</code> if not found
     */
    public Column getColumn(final String columnName) {
        if (columnName == null) {
            return null;
        }
        for (Column column : columns) {
            if (columnName.equals(column.getData())) {
                return column;
            }
        }
        return null;
    }

    /**
     * Add a new column.
     *
     * @param columnName the name of the column
     * @param searchable whether the column is searchable or not
     * @param orderable whether the column is orderable or not
     * @param searchValue if any, the search value to apply
     */
    public void addColumn(final String columnName, final boolean searchable,
                          final boolean orderable, final String searchValue) {
        this.columns.add(new Column(columnName, "", searchable, orderable,
                new Search(searchValue, false)));
    }

    /**
     * Add an order on the given column.
     *
     * @param columnName the name of the column
     * @param ascending whether the sorting is ascending or descending
     */
    public void addOrder(final String columnName, final boolean ascending) {
        if (columnName == null) {
            return;
        }
        for (int i = 0; i < columns.size(); i++) {
            if (!columnName.equals(columns.get(i).getData())) {
                continue;
            }
            order.add(new Order(i, ascending));
        }
    }

    /**
     * Gets the order column name, given the order ordinal value.
     * @return the order column name
     */
    public String getOrderColumnName() {
        // attempt to get the column property based on the order index.
        String orderColumnName = "id";
        List<Order> orders = getOrder();
        if (!CollectionUtils.isEmpty(orders)) {
            int orderColumnIndex = orders.get(0).getColumn();

            final Column column = getColumns().get(orderColumnIndex);

            // use the column's name as the order field for hibernate if set,
            // otherwise, use the columns' data field
            if (StringUtils.isNotEmpty(column.getName())) {
                orderColumnName = column.getName();
            } else {
                orderColumnName = column.getData();
            }
        }
        return orderColumnName;
    }


    /**
     * Generates a string for this object.
     * @return the string
     */
    @Override
    public String toString() {
        return "DataTableInput{"
                + "draw=" + draw
                + ", start=" + start
                + ", length=" + length
                + ", search=" + search
                + ", order=" + order
                + ", columns=" + columns
                + '}';
    }
}
