package ru.artemaa.stocks.web.stock;

import ru.artemaa.stocks.entity.Stock;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.DataSupplier;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.EntityOp;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

public class StockBrowse extends AbstractLookup {

    /**
     * The {@link CollectionDatasource} instance that loads a list of {@link Stock} records
     * to be displayed in {@link StockBrowse#stocksTable} on the left
     */
    @Inject
    private CollectionDatasource<Stock, UUID> stocksDs;

    /**
     * The {@link Datasource} instance that contains an instance of the selected entity
     * in {@link StockBrowse#stocksDs}
     * <p/> Containing instance is loaded in {@link CollectionDatasource#addItemChangeListener}
     * with the view, specified in the XML screen descriptor.
     * The listener is set in the {@link StockBrowse#init(Map)} method
     */
    @Inject
    private Datasource<Stock> stockDs;

    /**
     * The {@link Table} instance, containing a list of {@link Stock} records,
     * loaded via {@link StockBrowse#stocksDs}
     */
    @Inject
    private Table<Stock> stocksTable;

    /**
     * The {@link BoxLayout} instance that contains components on the left side
     * of {@link SplitPanel}
     */
    @Inject
    private BoxLayout lookupBox;

    /**
     * The {@link BoxLayout} instance that contains buttons to invoke Save or Cancel actions in edit mode
     */
    @Inject
    private BoxLayout actionsPane;

    /**
     * The {@link FieldGroup} instance that is linked to {@link StockBrowse#stockDs}
     * and shows fields of the selected {@link Stock} record
     */
    @Inject
    private FieldGroup fieldGroup;

    /**
     * The {@link RemoveAction} instance, related to {@link StockBrowse#stocksTable}
     */
    @Named("stocksTable.remove")
    private RemoveAction stocksTableRemove;

    @Inject
    private DataSupplier dataSupplier;

    /**
     * {@link Boolean} value, indicating if a new instance of {@link Stock} is being created
     */
    private boolean creating;

    @Override
    public void init(Map<String, Object> params) {

        /*
         * Adding {@link com.haulmont.cuba.gui.data.Datasource.ItemChangeListener} to {@link stocksDs}
         * The listener reloads the selected record with the specified view and sets it to {@link stockDs}
         */
        stocksDs.addItemChangeListener(e -> {
            if (e.getItem() != null) {
                Stock reloadedItem = dataSupplier.reload(e.getDs().getItem(), stockDs.getView());
                stockDs.setItem(reloadedItem);
            }
        });

        /*
         * Adding {@link CreateAction} to {@link stocksTable}
         * The listener removes selection in {@link stocksTable}, sets a newly created item to {@link stockDs}
         * and enables controls for record editing
         */
        stocksTable.addAction(new CreateAction(stocksTable) {
            @Override
            protected void internalOpenEditor(CollectionDatasource datasource, Entity newItem, Datasource parentDs, Map<String, Object> params) {
                stocksTable.setSelected(Collections.emptyList());
                stockDs.setItem((Stock) newItem);
                refreshOptionsForLookupFields();
                enableEditControls(true);
            }
        });

        /*
         * Adding {@link EditAction} to {@link stocksTable}
         * The listener enables controls for record editing
         */
        stocksTable.addAction(new EditAction(stocksTable) {
            @Override
            protected void internalOpenEditor(CollectionDatasource datasource, Entity existingItem, Datasource parentDs, Map<String, Object> params) {
                if (stocksTable.getSelected().size() == 1) {
                    refreshOptionsForLookupFields();
                    enableEditControls(false);
                }
            }

            @Override
            public void refreshState() {
                if (target != null) {
                    CollectionDatasource ds = target.getDatasource();
                    if (ds != null && !captionInitialized) {
                        setCaption(messages.getMainMessage("actions.Edit"));
                    }
                }
                super.refreshState();
            }

            @Override
            protected boolean isPermitted() {
                CollectionDatasource ownerDatasource = target.getDatasource();
                boolean entityOpPermitted = security.isEntityOpPermitted(ownerDatasource.getMetaClass(), EntityOp.UPDATE);
                if (!entityOpPermitted) {
                    return false;
                }
                return super.isPermitted();
            }
        });

        /*
         * Setting {@link RemoveAction#afterRemoveHandler} for {@link stocksTableRemove}
         * to reset record, contained in {@link stockDs}
         */
        stocksTableRemove.setAfterRemoveHandler(removedItems -> stockDs.setItem(null));

        disableEditControls();
    }

    private void refreshOptionsForLookupFields() {
        for (Component component : fieldGroup.getOwnComponents()) {
            if (component instanceof LookupField) {
                CollectionDatasource optionsDatasource = ((LookupField) component).getOptionsDatasource();
                if (optionsDatasource != null) {
                    optionsDatasource.refresh();
                }
            }
        }
    }

    /**
     * Method that is invoked by clicking Ok button after editing an existing or creating a new record
     */
    public void save() {
        if (!validate(Collections.singletonList(fieldGroup))) {
            return;
        }
        getDsContext().commit();

        Stock editedItem = stockDs.getItem();
        if (creating) {
            stocksDs.includeItem(editedItem);
        } else {
            stocksDs.updateItem(editedItem);
        }
        stocksTable.setSelected(editedItem);

        disableEditControls();
    }

    /**
     * Method that is invoked by clicking Cancel button, discards changes and disables controls for record editing
     */
    public void cancel() {
        Stock selectedItem = stocksDs.getItem();
        if (selectedItem != null) {
            Stock reloadedItem = dataSupplier.reload(selectedItem, stockDs.getView());
            stocksDs.setItem(reloadedItem);
        } else {
            stockDs.setItem(null);
        }

        disableEditControls();
    }

    /**
     * Enabling controls for record editing
     * @param creating indicates if a new instance of {@link Stock} is being created
     */
    private void enableEditControls(boolean creating) {
        this.creating = creating;
        initEditComponents(true);
        fieldGroup.requestFocus();
    }

    /**
     * Disabling editing controls
     */
    private void disableEditControls() {
        initEditComponents(false);
        stocksTable.requestFocus();
    }

    /**
     * Initiating edit controls, depending on if they should be enabled/disabled
     * @param enabled if true - enables editing controls and disables controls on the left side of the splitter
     *                if false - visa versa
     */
    private void initEditComponents(boolean enabled) {
        fieldGroup.setEditable(enabled);
        actionsPane.setVisible(enabled);
        lookupBox.setEnabled(!enabled);
    }
}