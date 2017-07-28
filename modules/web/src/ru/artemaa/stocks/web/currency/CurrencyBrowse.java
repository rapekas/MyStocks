package ru.artemaa.stocks.web.currency;

import ru.artemaa.stocks.entity.Currency;
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

public class CurrencyBrowse extends AbstractLookup {

    /**
     * The {@link CollectionDatasource} instance that loads a list of {@link Currency} records
     * to be displayed in {@link CurrencyBrowse#currenciesTable} on the left
     */
    @Inject
    private CollectionDatasource<Currency, UUID> currenciesDs;

    /**
     * The {@link Datasource} instance that contains an instance of the selected entity
     * in {@link CurrencyBrowse#currenciesDs}
     * <p/> Containing instance is loaded in {@link CollectionDatasource#addItemChangeListener}
     * with the view, specified in the XML screen descriptor.
     * The listener is set in the {@link CurrencyBrowse#init(Map)} method
     */
    @Inject
    private Datasource<Currency> currencyDs;

    /**
     * The {@link Table} instance, containing a list of {@link Currency} records,
     * loaded via {@link CurrencyBrowse#currenciesDs}
     */
    @Inject
    private Table<Currency> currenciesTable;

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
     * The {@link FieldGroup} instance that is linked to {@link CurrencyBrowse#currencyDs}
     * and shows fields of the selected {@link Currency} record
     */
    @Inject
    private FieldGroup fieldGroup;

    /**
     * The {@link RemoveAction} instance, related to {@link CurrencyBrowse#currenciesTable}
     */
    @Named("currenciesTable.remove")
    private RemoveAction currenciesTableRemove;

    @Inject
    private DataSupplier dataSupplier;

    /**
     * {@link Boolean} value, indicating if a new instance of {@link Currency} is being created
     */
    private boolean creating;

    @Override
    public void init(Map<String, Object> params) {

        /*
         * Adding {@link com.haulmont.cuba.gui.data.Datasource.ItemChangeListener} to {@link currenciesDs}
         * The listener reloads the selected record with the specified view and sets it to {@link currencyDs}
         */
        currenciesDs.addItemChangeListener(e -> {
            if (e.getItem() != null) {
                Currency reloadedItem = dataSupplier.reload(e.getDs().getItem(), currencyDs.getView());
                currencyDs.setItem(reloadedItem);
            }
        });

        /*
         * Adding {@link CreateAction} to {@link currenciesTable}
         * The listener removes selection in {@link currenciesTable}, sets a newly created item to {@link currencyDs}
         * and enables controls for record editing
         */
        currenciesTable.addAction(new CreateAction(currenciesTable) {
            @Override
            protected void internalOpenEditor(CollectionDatasource datasource, Entity newItem, Datasource parentDs, Map<String, Object> params) {
                currenciesTable.setSelected(Collections.emptyList());
                currencyDs.setItem((Currency) newItem);
                refreshOptionsForLookupFields();
                enableEditControls(true);
            }
        });

        /*
         * Adding {@link EditAction} to {@link currenciesTable}
         * The listener enables controls for record editing
         */
        currenciesTable.addAction(new EditAction(currenciesTable) {
            @Override
            protected void internalOpenEditor(CollectionDatasource datasource, Entity existingItem, Datasource parentDs, Map<String, Object> params) {
                if (currenciesTable.getSelected().size() == 1) {
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
         * Setting {@link RemoveAction#afterRemoveHandler} for {@link currenciesTableRemove}
         * to reset record, contained in {@link currencyDs}
         */
        currenciesTableRemove.setAfterRemoveHandler(removedItems -> currencyDs.setItem(null));

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

        Currency editedItem = currencyDs.getItem();
        if (creating) {
            currenciesDs.includeItem(editedItem);
        } else {
            currenciesDs.updateItem(editedItem);
        }
        currenciesTable.setSelected(editedItem);

        disableEditControls();
    }

    /**
     * Method that is invoked by clicking Cancel button, discards changes and disables controls for record editing
     */
    public void cancel() {
        Currency selectedItem = currenciesDs.getItem();
        if (selectedItem != null) {
            Currency reloadedItem = dataSupplier.reload(selectedItem, currencyDs.getView());
            currenciesDs.setItem(reloadedItem);
        } else {
            currencyDs.setItem(null);
        }

        disableEditControls();
    }

    /**
     * Enabling controls for record editing
     * @param creating indicates if a new instance of {@link Currency} is being created
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
        currenciesTable.requestFocus();
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