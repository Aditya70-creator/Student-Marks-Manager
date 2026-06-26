// ==========================================================================
// 1. GLOBAL BASE CONFIGURATIONS
// ==========================================================================
// The exact REST API endpoint address where our Java Controller is listening
const API_BASE_URL = 'https://student-marks-manager-uch5.onrender.com/api/students';
// DOM Element Registry (Grabbing all HTML nodes we need to interact with)
const studentForm = document.getElementById('student-form');
const studentIdInput = document.getElementById('student-id');
const studentNameInput = document.getElementById('student-name');
const rollNumberInput = document.getElementById('roll-number');
const physicsInput = document.getElementById('physics-marks');
const chemistryInput = document.getElementById('chemistry-marks');
const mathInput = document.getElementById('math-marks');

const submitBtn = document.getElementById('submit-btn');
const cancelBtn = document.getElementById('cancel-btn');
const formTitle = document.getElementById('form-title');

const searchInput = document.getElementById('search-input');
const searchBtn = document.getElementById('search-btn');
const clearSearchBtn = document.getElementById('clear-search-btn');

const tableBody = document.getElementById('students-table-body');
const alertMessage = document.getElementById('alert-message');

// ==========================================================================
// 2. EVENT LISTENERS SETUP
// ==========================================================================
// Initialize application data fetching when layout fully displays
document.addEventListener('DOMContentLoaded', fetchAllStudents);

// Handle Form Submissions (Both Saving New records and Updating Existing ones)
studentForm.addEventListener('submit', handleFormSubmit);

// Handle Search and Reset Operations
searchBtn.addEventListener('click', handleSearch);
clearSearchBtn.addEventListener('click', () => {
    searchInput.value = '';
    clearSearchBtn.classList.add('hidden');
    fetchAllStudents();
});

// Handle edit cancellation click
cancelBtn.addEventListener('click', resetFormState);

// ==========================================================================
// 3. CORE SERVICE FUNCTIONS (API NETWORK CALLS)
// ==========================================================================

// Function: GET operation to retrieve all recorded elements
async function fetchAllStudents() {
    try {
        const response = await fetch(API_BASE_URL);
        if (!response.ok) throw new Error('Failed to retrieve system records.');
        const students = await response.json();
        renderTableData(students);
    } catch (error) {
        showNotification(error.message, 'error');
    }
}

// Function: POST operation handling payload transmissions
async function handleFormSubmit(event) {
    event.preventDefault(); // Stop native page reload action

    // Package layout input details matching the Java model fields exactly
    const studentData = {
        name: studentNameInput.value.trim(),
        rollNumber: rollNumberInput.value.trim(),
        physicsMarks: parseFloat(physicsInput.value),
        chemistryMarks: parseFloat(chemistryInput.value),
        mathMarks: parseFloat(mathInput.value)
    };

    // If an ID exists in the hidden input box, attach it to perform an update update
    const existingId = studentIdInput.value;
    if (existingId) {
        studentData.id = existingId;
    }

    try {
        // Send payload via HTTP POST to the controller
        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(studentData)
        });

        if (!response.ok) throw new Error('Failed to process student entry documentation.');

        showNotification('Student profile transaction saved completely.', 'success');
        resetFormState();
        fetchAllStudents(); // Refresh data grid view
    } catch (error) {
        showNotification(error.message, 'error');
    }
}

// Function: GET operations specific to a particular Roll Number identity
async function handleSearch() {
    const query = searchInput.value.trim();
    if (!query) {
        showNotification('Please provide a roll query criteria input.', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/${query}`);
        if (!response.ok) throw new Error('Target record lookup failure.');
        
        const data = await response.json();
        if (data) {
            renderTableData([data]); // Convert single element to list for layout loop processing
            clearSearchBtn.classList.remove('hidden');
        } else {
            showNotification('No registry logs reflect that unique index tracking number.', 'error');
        }
    } catch (error) {
        showNotification('Record target data not located in database context.', 'error');
    }
}

// Function: DELETE processing triggers
async function deleteStudentRecord(id) {
    if (!confirm('Confirm deletion request? This operation cannot be rolled back.')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, { method: 'DELETE' });
        if (!response.ok) throw new Error('System deletion request rejected.');

        showNotification('Target entity wiped from data system register.', 'success');
        fetchAllStudents();
    } catch (error) {
        showNotification(error.message, 'error');
    }
}

// ==========================================================================
// 4. UI INTERACTIVE STATE MANIPULATION
// ==========================================================================

// Pre-fill the input fields with current values to enter "Edit Mode"
function initiateEditMode(studentJson) {
    const student = JSON.parse(decodeURIComponent(studentJson));
    
    formTitle.textContent = "Modify Student Record";
    submitBtn.textContent = "Apply Modification Updates";
    cancelBtn.classList.remove('hidden');

    // Make the Roll Number field read-only during edits to preserve database integrity
    rollNumberInput.setAttribute('readonly', 'true');

    // Fill the fields
    studentIdInput.value = student.id;
    studentNameInput.value = student.name;
    rollNumberInput.value = student.rollNumber;
    physicsInput.value = student.physicsMarks;
    chemistryInput.value = student.chemistryMarks;
    mathInput.value = student.mathMarks;
}

// Clear input items and restore normal non-editing mode flags
function resetFormState() {
    studentForm.reset();
    studentIdInput.value = '';
    formTitle.textContent = "Add New Student";
    submitBtn.textContent = "Save Student Record";
    cancelBtn.classList.add('hidden');
    rollNumberInput.removeAttribute('readonly');
}

// Build table text layouts mapping dynamic iterative datasets
function renderTableData(studentsList) {
    calculateDashboardMetrics(studentsList);
    tableBody.innerHTML = ''; // Wipe pre-existing array items visually

    if (studentsList.length === 0 || studentsList[0] === null) {
        tableBody.innerHTML = `<tr><td colspan="9" style="text-align: center; color: var(--text-secondary);">No administration records found inside active tracking ledger.</td></tr>`;
        return;
    }

    studentsList.forEach(student => {
        const tr = document.createElement('tr');
        // Package the student object string cleanly so it can be passed safely inside a button attribute string
        const serializedStudent = encodeURIComponent(JSON.stringify(student));

        tr.innerHTML = `
            <td><strong>${student.rollNumber}</strong></td>
            <td>${student.name}</td>
            <td>${student.physicsMarks}</td>
            <td>${student.chemistryMarks}</td>
            <td>${student.mathMarks}</td>
            <td>${student.totalMarks}</td>
            <td>${student.percentage}%</td>
            <td><span class="badge">${student.grade}</span></td>
            <td>
                <button class="btn btn-edit" onclick="initiateEditMode('${serializedStudent}')">Edit</button>
                <button class="btn btn-danger" onclick="deleteStudentRecord('${student.id}')">Delete</button>
            </td>
        `;
        tableBody.appendChild(tr);
    });
}

// Inject customized system warnings dynamically inside header space
function showNotification(message, statusType) {
    alertMessage.textContent = message;
    alertMessage.className = `alert alert-${statusType}`; // Swaps class names for theme adjustments
    
    // Auto-wipe context presentation elements smoothly after exactly 4.5 seconds pass
    setTimeout(() => {
        alertMessage.className = 'alert hidden';
    }, 4500);
}

// Function: Computes live arithmetic summary values from your current student collection array
function calculateDashboardMetrics(studentsList) {
    // Filter out null rows or empty states cleanly
    const activeStudents = studentsList.filter(s => s !== null && s.id);
    const totalCount = activeStudents.length;

    // Element references
    const statTotal = document.getElementById('stat-total');
    const statAverage = document.getElementById('stat-average');
    const statTop = document.getElementById('stat-top');

    if (totalCount === 0) {
        statTotal.innerText = '0';
        statAverage.innerText = '0.0%';
        statTop.innerText = 'N/A';
        return;
    }

    // 1. Calculate running Batch Percentage Average using array reduction
    const runningSum = activeStudents.reduce((sum, current) => sum + current.percentage, 0);
    const calculatedAverage = (runningSum / totalCount).toFixed(1);

    // 2. Identify the peak performer object via a standard comparison check
    let topStudent = activeStudents[0];
    activeStudents.forEach(student => {
        if (student.percentage > topStudent.percentage) {
            topStudent = student;
        }
    });

    // 3. Inject the calculated values into the card view elements
    statTotal.innerText = totalCount;
    statAverage.innerText = `${calculatedAverage}%`;
    statTop.innerText = topStudent.name.split(' ')[0]; // Show only the first name to prevent text overflow
}