// State
let items = [];
let itemDetails = {};

// Helper function to get cookie value
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
    return null;
}

// Helper function to get headers with session
function getHeaders() {
    const headers = {
        'Content-Type': 'application/json'
    };

    // Get JSESSIONID from cookie
    const jsessionId = getCookie('JSESSIONID');
    if (jsessionId) {
        headers['JSESSION'] = jsessionId;
    }

    return headers;
}

// Helper function for fetch with session
async function fetchWithSession(url, options = {}) {
    const headers = getHeaders();

    const config = {
        ...options,
        headers: {
            ...headers,
            ...options.headers
        },
        credentials: 'include'
    };

    return fetch(url, config);
}

// Check session on load
window.addEventListener('load', () => {
    const jsessionId = getCookie('JSESSIONID');
    if (!jsessionId) {
        console.warn('No JSESSIONID cookie found');
    }
    loadItems();
});

// Show toast
function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    const toastMessage = document.getElementById('toastMessage');
    toastMessage.textContent = message;
    toast.className = `toast ${type} show`;
    toast.querySelector('i').className = type === 'success' ? 'fas fa-check-circle' :
        type === 'error' ? 'fas fa-exclamation-circle' : 'fas fa-info-circle';
    setTimeout(() => toast.classList.remove('show'), 4000);
}

// Load all items
async function loadItems() {
    const loadingState = document.getElementById('loadingState');
    const emptyState = document.getElementById('emptyState');
    const itemsGrid = document.getElementById('itemsGrid');

    loadingState.style.display = 'flex';
    emptyState.style.display = 'none';
    itemsGrid.style.display = 'none';

    try {
        const response = await fetchWithSession('http://localhost:8080/item', {
            method: 'GET'
        });

        if (response.status === 401 || response.status === 403) {
            window.location.href = 'login.html';
            return;
        }

        if (!response.ok) {
            throw new Error('Failed to load items');
        }

        const allItems = await response.json();

        // Filter out items where isDeleted = true (only show active items)
        items = allItems.filter(item => !item.isDeleted);

        loadingState.style.display = 'none';

        if (items.length === 0) {
            emptyState.style.display = 'flex';
            itemsGrid.style.display = 'none';
        } else {
            emptyState.style.display = 'none';
            itemsGrid.style.display = 'grid';
            renderItems(items);
            updateStats(items);
        }

    } catch (err) {
        console.error('Error loading items:', err);
        loadingState.style.display = 'none';
        showToast('Failed to load items. Please try again.', 'error');
    }
}

// Fetch item details
async function fetchItemDetails(itemId) {
    if (itemDetails[itemId]) {
        return itemDetails[itemId];
    }

    try {
        const response = await fetchWithSession(`http://localhost:8080/itemdetails/${itemId}`, {
            method: 'GET'
        });

        if (!response.ok) {
            if (response.status === 404) {
                return null;
            }
            throw new Error('Failed to load details');
        }

        const details = await response.json();
        itemDetails[itemId] = details;
        return details;

    } catch (err) {
        console.error(`Error loading details for item ${itemId}:`, err);
        return null;
    }
}

// Render items
function renderItems(itemsToRender) {
    const grid = document.getElementById('itemsGrid');
    grid.innerHTML = '';

    itemsToRender.forEach(item => {
        const card = document.createElement('div');
        card.className = 'item-card'; // Removed 'deleted' class since we only show active items
        card.id = `item-${item.id}`;

        card.innerHTML = `
            <div class="item-header">
                <span class="item-id-badge">#${item.id}</span>
                <div class="item-status"></div>
            </div>
            <div class="item-body">
                <div class="item-name">${escapeHtml(item.name)}</div>
                <div class="item-meta">
                    <div class="meta-item">
                        <i class="fas fa-tag"></i>
                        <span>Price: <span class="value">$${item.price.toFixed(2)}</span></span>
                    </div>
                    <div class="meta-item">
                        <i class="fas fa-cubes"></i>
                        <span>Stock: <span class="value">${item.totalNumber}</span></span>
                    </div>
                </div>
            </div>
            <div class="item-actions">
                <button class="btn-action btn-update" onclick="openUpdateModal(${item.id})">
                    <i class="fas fa-edit"></i> Update
                </button>
                <button class="btn-action btn-delete" onclick="deleteItem(${item.id})">
                    <i class="fas fa-trash"></i> Delete
                </button>
            </div>
            <div class="item-actions" style="margin-top: 0; padding-top: 0;">
                <button class="btn-action btn-add-details" onclick="openAddDetailsModal(${item.id})">
                    <i class="fas fa-plus-circle"></i> Add Details
                </button>
                <button class="btn-action btn-update-details" onclick="openUpdateDetailsModal(${item.id})">
                    <i class="fas fa-clipboard-list"></i> Update Details
                </button>
                <button class="btn-action btn-delete" onclick="deleteItemDetails(${item.id})">
                    <i class="fas fa-trash"></i> Delete Details
                </button>
            </div>
            <div class="details-toggle" onclick="toggleDetails(${item.id}, this)">
                <span>Show Details</span>
                <i class="fas fa-chevron-down"></i>
            </div>
            <div class="details-content" id="details-${item.id}">
                <div class="details-grid" id="details-grid-${item.id}">
                    <div class="detail-row">
                        <div class="detail-label"><i class="fas fa-spinner fa-spin"></i> Loading details...</div>
                    </div>
                </div>
            </div>
        `;

        grid.appendChild(card);
    });
}

// Toggle details dropdown
async function toggleDetails(itemId, toggleEl) {
    const content = document.getElementById(`details-${itemId}`);
    const isOpen = content.classList.contains('open');

    if (isOpen) {
        content.classList.remove('open');
        toggleEl.classList.remove('open');
        toggleEl.querySelector('span').textContent = 'Show Details';
    } else {
        content.classList.add('open');
        toggleEl.classList.add('open');
        toggleEl.querySelector('span').textContent = 'Hide Details';

        const details = await fetchItemDetails(itemId);
        const grid = document.getElementById(`details-grid-${itemId}`);

        if (details) {
            grid.innerHTML = `
                <div class="detail-row">
                    <div class="detail-label"><i class="fas fa-align-left"></i> Description</div>
                    <div class="detail-value">${escapeHtml(details.description)}</div>
                </div>
                <div class="detail-row">
                    <div class="detail-label"><i class="fas fa-calendar-plus"></i> Issued At</div>
                    <div class="detail-value">${formatDate(details.issuedAt)}</div>
                </div>
                <div class="detail-row">
                    <div class="detail-label"><i class="fas fa-calendar-times"></i> Expires At</div>
                    <div class="detail-value">${formatDate(details.expiredAt)}</div>
                </div>
            `;
        } else {
            grid.innerHTML = `
                <div class="detail-row">
                    <div class="detail-label"><i class="fas fa-info-circle"></i> No details available</div>
                    <div class="detail-value" style="color: rgba(255,255,255,0.5);">Click "Add Details" to create</div>
                </div>
            `;
        }
    }
}

// Update stats
function updateStats(activeItems) {
    const totalItems = activeItems.length;
    const totalValue = activeItems.reduce((sum, item) => sum + (item.price * item.totalNumber), 0);
    const totalStock = activeItems.reduce((sum, item) => sum + item.totalNumber, 0);

    document.getElementById('totalItems').textContent = totalItems;
    document.getElementById('totalValue').textContent = '$' + totalValue.toFixed(2);
    document.getElementById('totalStock').textContent = totalStock;
}

// Delete item (soft delete - just sets isDeleted flag to true)
async function deleteItem(itemId) {
    if (!confirm('Are you sure you want to delete this item?')) {
        return;
    }

    // Disable delete button to prevent multiple clicks
    const deleteBtn = event?.target?.closest('.btn-delete');
    if (deleteBtn) {
        deleteBtn.disabled = true;
        deleteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Deleting...';
    }

    try {
        const response = await fetchWithSession(`http://localhost:8080/item/${itemId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showToast('Item deleted successfully', 'success');

            // Remove the item from the local items array
            const deletedItemIndex = items.findIndex(i => i.id === itemId);
            if (deletedItemIndex !== -1) {
                items.splice(deletedItemIndex, 1);
            }

            // Remove the card from UI with animation
            const card = document.getElementById(`item-${itemId}`);
            if (card) {
                card.style.transform = 'scale(0.9)';
                card.style.opacity = '0';
                setTimeout(() => {
                    card.remove();
                    // Update stats after removal
                    updateStats(items);
                    // Check if no items left
                    if (items.length === 0) {
                        document.getElementById('emptyState').style.display = 'flex';
                        document.getElementById('itemsGrid').style.display = 'none';
                    }
                }, 300);
            } else {
                // If card not found, just reload items
                loadItems();
            }
        } else {
            const errorText = await response.text();
            showToast(errorText || 'Failed to delete item', 'error');
        }
    } catch (err) {
        console.error('Error deleting item:', err);
        showToast('Network error. Please try again.', 'error');
    } finally {
        if (deleteBtn) {
            deleteBtn.disabled = false;
            deleteBtn.innerHTML = '<i class="fas fa-trash"></i> Delete';
        }
    }
}

// Update modal
function openUpdateModal(itemId) {
    const item = items.find(i => i.id === itemId);
    if (!item) return;

    document.getElementById('updateItemId').value = itemId;
    document.getElementById('updateName').value = item.name;
    document.getElementById('updatePrice').value = item.price;
    document.getElementById('updateStock').value = item.totalNumber;

    // Clear any previous error states
    document.getElementById('updateName').classList.remove('error');
    document.getElementById('updateModal').classList.add('active');
}

async function submitUpdate() {
    const itemId = parseInt(document.getElementById('updateItemId').value);
    const name = document.getElementById('updateName').value.trim();
    const price = parseFloat(document.getElementById('updatePrice').value);
    const stock = parseInt(document.getElementById('updateStock').value);

    if (!name || isNaN(price) || isNaN(stock)) {
        showToast('Please fill all fields correctly', 'error');
        return;
    }

    // Get the existing item to preserve userId and other fields
    const existingItem = items.find(i => i.id === itemId);
    if (!existingItem) {
        showToast('Item not found', 'error');
        return;
    }

    // Disable submit button to prevent multiple submissions
    const submitBtn = document.querySelector('#updateModal .modal-btn-primary');
    const originalText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Updating...';
    submitBtn.disabled = true;

    try {
        const response = await fetchWithSession('http://localhost:8080/item', {
            method: 'PUT',
            body: JSON.stringify({
                id: itemId,
                name: name,
                price: price,
                totalNumber: stock,
                isDeleted: false, // Keep as active since we're updating
                userId: existingItem.userId
            })
        });

        if (response.ok) {
            showToast('Item updated successfully', 'success');
            closeModal('updateModal');
            loadItems(); // Reload items to get updated data
        } else if (response.status === 400) {
            const errorText = await response.text();
            // Check if the error is about duplicate name
            if (errorText && errorText.includes('duplicate key value violates unique constraint "item_name_unique"')) {
                showToast('This item name already exists! Please use a different name.', 'error');
                // Highlight the name field to indicate error
                const nameInput = document.getElementById('updateName');
                nameInput.classList.add('error');
                // Shake the input field
                nameInput.style.animation = 'shakeInput 0.4s ease-in-out';
                setTimeout(() => {
                    nameInput.style.animation = '';
                }, 400);
                // Remove error class on input
                setTimeout(() => {
                    nameInput.classList.remove('error');
                }, 3000);
                // Focus on the name field
                nameInput.focus();
            } else {
                showToast(errorText || 'Failed to update item', 'error');
            }
        } else {
            const errorText = await response.text();
            showToast(errorText || 'Failed to update item', 'error');
        }
    } catch (err) {
        console.error('Error updating item:', err);
        showToast('Network error. Please try again.', 'error');
    } finally {
        // Re-enable submit button
        submitBtn.innerHTML = originalText;
        submitBtn.disabled = false;
    }
}

// Add modal
function openAddModal() {
    document.getElementById('addName').value = '';
    document.getElementById('addPrice').value = '';
    document.getElementById('addStock').value = '';
    // Clear any previous error states
    document.getElementById('addName').classList.remove('error');
    document.getElementById('addModal').classList.add('active');
}

async function submitAdd() {
    const name = document.getElementById('addName').value.trim();
    const price = parseFloat(document.getElementById('addPrice').value);
    const stock = parseInt(document.getElementById('addStock').value);

    if (!name || isNaN(price) || isNaN(stock)) {
        showToast('Please fill all fields correctly', 'error');
        return;
    }

    // Disable submit button to prevent multiple submissions
    const submitBtn = document.querySelector('#addModal .modal-btn-primary');
    const originalText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Adding...';
    submitBtn.disabled = true;

    try {
        const response = await fetchWithSession('http://localhost:8080/item', {
            method: 'POST',
            body: JSON.stringify({
                name: name,
                price: price,
                totalNumber: stock
            })
        });

        if (response.ok) {
            showToast('Item added successfully', 'success');
            closeModal('addModal');
            loadItems(); // Reload items to show the new item
        } else if (response.status === 400) {
            const errorText = await response.text();
            // Check if the error is about duplicate name
            if (errorText && errorText.includes('duplicate key value violates unique constraint "item_name_unique"')) {
                showToast('This item name already exists! Please use a different name.', 'error');
                // Highlight the name field to indicate error
                const nameInput = document.getElementById('addName');
                nameInput.classList.add('error');
                // Shake the input field
                nameInput.style.animation = 'shakeInput 0.4s ease-in-out';
                setTimeout(() => {
                    nameInput.style.animation = '';
                }, 400);
                // Remove error class on input
                setTimeout(() => {
                    nameInput.classList.remove('error');
                }, 3000);
                // Focus on the name field
                nameInput.focus();
            } else {
                showToast(errorText || 'Failed to add item', 'error');
            }
        } else {
            const errorText = await response.text();
            showToast(errorText || 'Failed to add item', 'error');
        }
    } catch (err) {
        console.error('Error adding item:', err);
        showToast('Network error. Please try again.', 'error');
    } finally {
        // Re-enable submit button
        submitBtn.innerHTML = originalText;
        submitBtn.disabled = false;
    }
}

// Add Item Details Modal
function openAddDetailsModal(itemId) {
    document.getElementById('detailsItemId').value = itemId;
    document.getElementById('addDescription').value = '';
    document.getElementById('addIssuedAt').value = '';
    document.getElementById('addExpiredAt').value = '';
    document.getElementById('addDetailsModal').classList.add('active');
}

// Add item details
async function submitAddDetails() {
    const itemId = document.getElementById('detailsItemId').value;
    const description = document.getElementById('addDescription').value.trim();
    const issuedAt = document.getElementById('addIssuedAt').value;
    const expiredAt = document.getElementById('addExpiredAt').value;

    if (!description || !issuedAt || !expiredAt) {
        showToast('Please fill all fields', 'error');
        return;
    }

    // Disable submit button to prevent multiple submissions
    const submitBtn = document.querySelector('#addDetailsModal .modal-btn-primary');
    const originalText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Adding...';
    submitBtn.disabled = true;

    try {
        const response = await fetchWithSession('http://localhost:8080/itemdetails', {
            method: 'POST',
            body: JSON.stringify({
                itemId: itemId,
                description: description,
                issuedAt: issuedAt,
                expiredAt: expiredAt
            })
        });

        if (response.ok) {
            showToast('Item details added successfully', 'success');
            closeModal('addDetailsModal');
            delete itemDetails[itemId];

            const detailsContent = document.getElementById(`details-${itemId}`);
            if (detailsContent && detailsContent.classList.contains('open')) {
                const toggleEl = document.querySelector(`[onclick*="toggleDetails(${itemId}"]`);
                if (toggleEl) {
                    detailsContent.classList.remove('open');
                    toggleEl.classList.remove('open');
                    toggleEl.querySelector('span').textContent = 'Show Details';
                    setTimeout(() => toggleDetails(itemId, toggleEl), 100);
                }
            }
        } else {
            const errorText = await response.text();
            showToast(errorText || 'Failed to add details', 'error');
        }
    } catch (err) {
        console.error('Error adding item details:', err);
        showToast('Network error. Please try again.', 'error');
    } finally {
        submitBtn.innerHTML = originalText;
        submitBtn.disabled = false;
    }
}

// Update Item Details Modal
async function openUpdateDetailsModal(itemId) {
    const details = await fetchItemDetails(itemId);

    if (!details) {
        showToast('No details exist for this item. Please add details first.', 'info');
        return;
    }

    document.getElementById('updateDetailsItemId').value = itemId;
    document.getElementById('updateDescription').value = details.description || '';
    document.getElementById('updateIssuedAt').value = details.issuedAt || '';
    document.getElementById('updateExpiredAt').value = details.expiredAt || '';

    document.getElementById('updateDetailsModal').classList.add('active');
}

async function submitUpdateDetails() {
    const itemId = document.getElementById('updateDetailsItemId').value;
    const description = document.getElementById('updateDescription').value.trim();
    const issuedAt = document.getElementById('updateIssuedAt').value;
    const expiredAt = document.getElementById('updateExpiredAt').value;

    if (!description || !issuedAt || !expiredAt) {
        showToast('Please fill all fields', 'error');
        return;
    }

    // Disable submit button to prevent multiple submissions
    const submitBtn = document.querySelector('#updateDetailsModal .modal-btn-primary');
    const originalText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Updating...';
    submitBtn.disabled = true;

    try {
        // PUT to /itemdetails/{itemId}
        const response = await fetchWithSession(`http://localhost:8080/itemdetails/${itemId}`, {
            method: 'PUT',
            body: JSON.stringify({
                itemId: itemId,
                description: description,
                issuedAt: issuedAt,
                expiredAt: expiredAt
            })
        });

        if (response.ok) {
            showToast('Item details updated successfully', 'success');
            closeModal('updateDetailsModal');
            delete itemDetails[itemId];

            const detailsContent = document.getElementById(`details-${itemId}`);
            if (detailsContent && detailsContent.classList.contains('open')) {
                const toggleEl = document.querySelector(`[onclick*="toggleDetails(${itemId}"]`);
                if (toggleEl) {
                    detailsContent.classList.remove('open');
                    toggleEl.classList.remove('open');
                    toggleEl.querySelector('span').textContent = 'Show Details';
                    setTimeout(() => toggleDetails(itemId, toggleEl), 100);
                }
            }
        } else {
            const errorText = await response.text();
            showToast(errorText || 'Failed to update details', 'error');
        }
    } catch (err) {
        console.error('Error updating item details:', err);
        showToast('Network error. Please try again.', 'error');
    } finally {
        submitBtn.innerHTML = originalText;
        submitBtn.disabled = false;
    }
}

// Delete item details
async function deleteItemDetails(itemId) {
    if (!confirm('Are you sure you want to delete the details for this item?')) {
        return;
    }

    try {
        const response = await fetchWithSession(`http://localhost:8080/itemdetails/${itemId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showToast('Item details deleted successfully', 'success');
            delete itemDetails[itemId];

            const detailsContent = document.getElementById(`details-${itemId}`);
            if (detailsContent && detailsContent.classList.contains('open')) {
                const grid = document.getElementById(`details-grid-${itemId}`);
                grid.innerHTML = `
                    <div class="detail-row">
                        <div class="detail-label"><i class="fas fa-info-circle"></i> No details available</div>
                        <div class="detail-value" style="color: rgba(255,255,255,0.5);">Click "Add Details" to create</div>
                    </div>
                `;
            }
        } else {
            throw new Error('Failed to delete details');
        }
    } catch (err) {
        console.error('Error deleting item details:', err);
        showToast('Failed to delete item details', 'error');
    }
}

// Close modal
function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('active');
}

// Close modal on overlay click
document.querySelectorAll('.modal-overlay').forEach(overlay => {
    overlay.addEventListener('click', (e) => {
        if (e.target === overlay) {
            overlay.classList.remove('active');
        }
    });
});

// Logout - Updated to call the logout endpoint
async function logout() {
    try {
        const response = await fetchWithSession('http://localhost:8080/logout', {
            method: 'GET'
        });

        if (response.ok) {
            // Clear all session data
            // Clear JSESSIONID cookie
            document.cookie = 'JSESSIONID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';

            // Clear any other session-related data
            sessionStorage.clear();
            localStorage.clear();

            // Reset items array and details cache
            items = [];
            itemDetails = {};

            // Show success message
            showToast('Logged out successfully!', 'success');

            // Redirect to login page after a brief delay
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 1000);
        } else {
            // Even if the response is not OK, still redirect to login
            console.warn('Logout response not OK, still redirecting to login');
            document.cookie = 'JSESSIONID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
            window.location.href = 'login.html';
        }
    } catch (err) {
        console.error('Logout error:', err);
        // On error, still clear session and redirect
        document.cookie = 'JSESSIONID=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
        showToast('Logged out! Redirecting...', 'info');
        setTimeout(() => {
            window.location.href = 'login.html';
        }, 1000);
    }
}

// Helpers
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function formatDate(dateStr) {
    if (!dateStr) return 'N/A';
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    });
}