// ==========================================================================
// 1. GLOBAL BASE CONFIGURATIONS & RUNTIME VERIFICATION
// ==========================================================================
const API_ACADEMIC_URL = 'https://student-marks-manager-uch5.onrender.com/api/academic';

// Session Validation: Verify user profile context exists in local storage
const currentUserId = localStorage.getItem('tracker_user_id');
const currentUsername = localStorage.getItem('tracker_user_name');

if (!currentUserId) {
    // If identity token is blank, redirect user immediately to the auth gate
    window.location.href = 'index.html';
}

// Element Bindings Registry
const displayUsername = document.getElementById('display-username');
const subjectRowsVault = document.getElementById('dynamic-subject-rows-vault');
const compilerForm = document.getElementById('semester-compiler-form');
const selectSemesterName = document.getElementById('select-semester-name');

const metricCgpa = document.getElementById('metric-cgpa');
const metricSgpa = document.getElementById('metric-sgpa');
const metricHighest = document.getElementById('metric-highest');
const metricCredits = document.getElementById('metric-credits');
const semesterTimelineVault = document.getElementById('semester-timeline-vault');

// Initialize Dashboard Bindings when document enters fully loaded state
document.addEventListener('DOMContentLoaded', () => {
    if (displayUsername) displayUsername.innerText = currentUsername || 'Authenticated Student';
    // Load existing history entries and aggregate numbers from microservices
    loadDashboardData();
    // Pre-populate with 3 clean subject rows so the form doesn't mount empty
    if (subjectRowsVault) {
        for (let i = 0; i < 3; i++) appendNewSubjectRowField();
    }
});

// ==========================================================================
// 2. DYNAMIC INPUT FIELD MANAGEMENT
// ==========================================================================
function appendNewSubjectRowField() {
    const rowId = 'row-' + Date.now() + '-' + Math.random().toString(36).slice(2, 7);
    
    const rowDiv = document.createElement('div');
    rowDiv.className = 'form-row';
    rowDiv.id = rowId;
    
    rowDiv.innerHTML = `
        <input type="text" class="field-sub-name" placeholder="e.g., Computer Networks" required>
        <input type="number" class="field-sub-scored" placeholder="Marks" min="0" max="100" step="0.1" required>
        <input type="number" class="field-sub-max" placeholder="Max" min="1" required value="100">
        <select class="field-sub-credits" required>
            <option value="1">1 Credit</option>
            <option value="2">2 Credits</option>
            <option value="3">3 Credits</option>
            <option value="4" selected>4 Credits</option>
            <option value="5">5 Credits</option>
        </select>
        <button type="button" class="btn-icon-danger" onclick="removeSubjectRowField('${rowId}')">✕</button>
    `;
    
    if (subjectRowsVault) subjectRowsVault.appendChild(rowDiv);
}

function removeSubjectRowField(id) {
    const targetRow = document.getElementById(id);
    // Ensure required elements exist
    if (!subjectRowsVault) return;
    if (!targetRow) return;
    // Ensure the form retains at least one active field row segment
    if (subjectRowsVault.children.length > 1) {
        subjectRowsVault.removeChild(targetRow);
    } else {
        showToast("An academic term entry must contain at least one subject profile.", "error");
    }
}

// ==========================================================================
// 3. CORE SERVICE METHODS: DATA TRANSFERS & COMPILATION
// ==========================================================================
if (compilerForm) {
    compilerForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        if (!subjectRowsVault) return showToast('Form is not properly initialized.', 'error');

        const rowElements = subjectRowsVault.getElementsByClassName('form-row');
        const subjectsArray = [];

        // Parse data out of each dynamic input element row block
        for (let row of rowElements) {
            const nameEl = row.querySelector('.field-sub-name');
            const scoredEl = row.querySelector('.field-sub-scored');
            const maxEl = row.querySelector('.field-sub-max');
            const creditsEl = row.querySelector('.field-sub-credits');
            if (!nameEl || !scoredEl || !maxEl || !creditsEl) continue;

            const name = nameEl.value.trim();
            const scored = parseFloat(scoredEl.value) || 0;
            const max = parseFloat(maxEl.value) || 0;
            const credits = parseInt(creditsEl.value) || 0;

            if (max > 0 && scored > max) {
                showToast(`Scored marks cannot eclipse scale maximums for "${name}".`, "error");
                return;
            }

            subjectsArray.push({
                name: name,
                marksObtained: scored,
                maxMarks: max,
                credits: credits
            });
        }

        // Package root parent Semester object
        const semesterPayload = {
            userId: currentUserId,
            semesterName: selectSemesterName ? selectSemesterName.value : '',
            subjects: subjectsArray
        };

        try {
            const response = await fetch(`${API_ACADEMIC_URL}/save`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(semesterPayload)
            });

            if (!response.ok) throw new Error("Failed to process semester document persistence execution.");

            showToast("Semester academic metrics committed successfully.", "success");
            
            // Reset and clear layout forms
            compilerForm.reset();
            if (subjectRowsVault) subjectRowsVault.innerHTML = '';
            for (let i = 0; i < 3; i++) appendNewSubjectRowField();
            
            // Refresh analytics outputs
            loadDashboardData();

        } catch (error) {
            showToast(error.message || 'Submission failed', "error");
        }
    });
}

async function loadDashboardData() {
    try {
        // Task 1: Fetch Aggregate KPI variables from analytics pipeline endpoints
        const analyticsRes = await fetch(`${API_ACADEMIC_URL}/analytics/${currentUserId}`);
        if (analyticsRes && analyticsRes.ok) {
            const analytics = await analyticsRes.json();
            const cgpa = analytics && typeof analytics.cgpa === 'number' ? analytics.cgpa : 0;
            const sgpa = analytics && typeof analytics.currentSgpa === 'number' ? analytics.currentSgpa : 0;
            const highest = analytics && typeof analytics.highestSgpa === 'number' ? analytics.highestSgpa : 0;
            const credits = analytics && (typeof analytics.totalCredits === 'number' || typeof analytics.totalCredits === 'string') ? analytics.totalCredits : 0;

            if (metricCgpa) metricCgpa.innerText = (Number(cgpa) || 0).toFixed(2);
            if (metricSgpa) metricSgpa.innerText = (Number(sgpa) || 0).toFixed(2);
            if (metricHighest) metricHighest.innerText = (Number(highest) || 0).toFixed(2);
            if (metricCredits) metricCredits.innerText = credits;
        }

        // Task 2: Fetch Historic Timeline Records list data arrays
        const historyRes = await fetch(`${API_ACADEMIC_URL}/user/${currentUserId}`);
        if (historyRes && historyRes.ok) {
            const semestersList = await historyRes.json();
            renderHistoryCards(semestersList);
        }

    } catch (error) {
        showToast("Error communicating with data collection microservices.", "error");
    }
}

// ==========================================================================
// 4. RENDERING & NOTIFICATION UTILITIES
// ==========================================================================
function renderHistoryCards(semesters) {
    if (!semesterTimelineVault) return;
    if (!semesters || semesters.length === 0) {
        semesterTimelineVault.innerHTML = `<div style="color:var(--text-muted); font-size:0.9rem; text-align:center; padding:2rem 0;">No semester history mapped to this tracking profile.</div>`;
        return;
    }

    semesterTimelineVault.innerHTML = ''; // Clear stale cards

    // Display history starting from the most recent tracking blocks down to oldest entries
    semesters.slice().reverse().forEach(sem => {
        const card = document.createElement('div');
        card.className = 'semester-card-node';

        const sgpa = (sem && (typeof sem.sgpa === 'number' || typeof sem.sgpa === 'string')) ? Number(sem.sgpa) : 0;
        const credits = sem && (sem.totalCredits !== undefined) ? sem.totalCredits : 0;
        const subjCount = Array.isArray(sem && sem.subjects) ? sem.subjects.length : 0;

        card.innerHTML = `
            <div class="sem-node-header">
                <span class="sem-node-title">${sem && sem.semesterName ? sem.semesterName : 'Semester'}</span>
                <span class="sem-node-metric">SGPA: ${Number(sgpa).toFixed(2)}</span>
            </div>
            <div class="sem-node-subtext">
                Aggregated Units: ${credits} Credits Attempted • Contains ${subjCount} Evaluated Courses
            </div>
        `;
        semesterTimelineVault.appendChild(card);
    });
}

function executeSystemLogout() {
    localStorage.removeItem('tracker_user_id');
    localStorage.removeItem('tracker_user_name');
    window.location.href = 'index.html';
}

function showToast(message, type) {
    const alertBox = document.getElementById('toast-alert');
    if (!alertBox) {
        // Fallback when toast container is missing
        try { alert(message); } catch (e) { console.log(type, message); }
        return;
    }
    alertBox.innerText = message;
    alertBox.className = `toast-banner toast-${type} toast-active`;
    
    setTimeout(() => {
        if (alertBox && alertBox.classList) alertBox.classList.remove('toast-active');
    }, 4000);
}

// ==========================================================================
// STEP 15: FUTURE-READY MODULAR ROUTING PROXIES
// ==========================================================================
async function triggerExtension(moduleKey) {
    showToast(`Initializing ${moduleKey.toUpperCase()} system handshake protocol...`, "success");
    
    try {
        const response = await fetch(`http://localhost:8080/api/extensions/${moduleKey}/${currentUserId}`);
        const data = await response.json();
        
        // When the feature is offline, our backend will throw an intentional status response
        if (response.status === 501) {
            showToast(data && data.message ? data.message : 'Feature not implemented', "error");
        } else if (!response.ok) {
            showToast(data && data.message ? data.message : 'Extension request failed', 'error');
        } else {
            showToast(`Data processed successfully by ${moduleKey.toUpperCase()} engine.`, "success");
        }
    } catch (error) {
        showToast("Unable to patch connection through to target intelligence engine.", "error");
    }
}