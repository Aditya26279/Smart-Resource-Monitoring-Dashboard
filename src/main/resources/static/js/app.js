// app.js

/**
 * 1. Global Chart Setup
 * We initialize vanilla Chart.js arrays with neon gradients for the visual "wow" factor
 */
let cpuData = Array(20).fill(0);
let memData = Array(20).fill(0);
let diskData = Array(20).fill(0);

const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    animation: { duration: 0 },
    scales: {
        x: { display: false },
        y: { 
            beginAtZero: true, 
            max: 100, 
            grid: { color: 'rgba(255, 255, 255, 0.05)' } 
        }
    },
    plugins: { legend: { display: false } }
};

const createChart = (canvasId, initialData, borderColor, bgColor, fixedMax = null) => {
    let ctx = document.getElementById(canvasId).getContext('2d');
    
    // Gradient definitions for modern glassy look
    let gradient = ctx.createLinearGradient(0, 0, 0, 200);
    gradient.addColorStop(0, bgColor);
    gradient.addColorStop(1, 'rgba(0,0,0,0)');

    let customOptions = JSON.parse(JSON.stringify(chartOptions));
    if (fixedMax) customOptions.scales.y.max = fixedMax;
    else delete customOptions.scales.y.max; // auto-scale for things like disk IO

    return new Chart(ctx, {
        type: 'line',
        data: {
            labels: Array(20).fill(''),
            datasets: [{
                data: initialData,
                borderColor: borderColor,
                backgroundColor: gradient,
                borderWidth: 2,
                pointRadius: 0,
                fill: true,
                tension: 0.4
            }]
        },
        options: customOptions
    });
};

// Initialize Charts
const cpuGraph = createChart('cpuChart', cpuData, '#58a6ff', 'rgba(88, 166, 255, 0.3)', 100);
const memGraph = createChart('memoryChart', memData, '#238636', 'rgba(35, 134, 54, 0.3)', 100);
const diskGraph = createChart('diskChart', diskData, '#f85149', 'rgba(248, 81, 73, 0.3)');

/**
 * 2. Real-Time Communication Layer (WebSocket / Phase 3 Integration)
 */
let stompClient = null;

function connectWebsocket() {
    // Uses underlying Spring STOMP messaging route established in Phase 3
    let socket = new SockJS('/ws/metrics');
    stompClient = Stomp.over(socket);
    stompClient.debug = null; // suppress dev logs
    
    stompClient.connect({}, function (frame) {
        updateConnectionStatus(true);
        stompClient.subscribe('/topic/metrics', function (message) {
            handleIncomingMetrics(JSON.parse(message.body));
        });
    }, function(error) {
        updateConnectionStatus(false);
        setTimeout(connectWebsocket, 5000); // auto-reconnect
    });
}

function updateConnectionStatus(isConnected) {
    const badge = document.getElementById('ws-status');
    const pulse = document.querySelector('.pulse-indicator');
    if (isConnected) {
        badge.textContent = "Connected";
        badge.className = "status-badge connected";
        pulse.style.backgroundColor = 'var(--accent-green)';
        pulse.style.boxShadow = '0 0 10px var(--accent-green)';
    } else {
        badge.textContent = "Disconnected";
        badge.className = "status-badge disconnected";
        pulse.style.backgroundColor = 'var(--accent-red)';
        pulse.style.boxShadow = '0 0 10px var(--accent-red)';
    }
}

function handleIncomingMetrics(metricsArray) {
    let cpuVal = 0;
    let memVal = 0;
    let diskRead = 0;
    
    metricsArray.forEach(m => {
        if(m.metricType === "CPU" && m.metricName === "CPU_LOAD") {
            cpuVal = m.metricValue;
            document.getElementById('currentCpuTxt').innerText = cpuVal.toFixed(2) + "%";
        }
        else if(m.metricType === "MEMORY" && m.metricName === "USED_MEMORY") {
            memVal = m.metricValue;
            document.getElementById('currentMemoryTxt').innerText = memVal.toFixed(1) + "%";
        }
        else if(m.metricType === "DISK" && m.metricName === "DISK_READS") {
            // disk stream normalized (MB/s mapping roughly)
            diskRead = (m.metricValue / (1024*1024)).toFixed(2);
            document.getElementById('currentDiskTxt').innerText = diskRead + " MB Read";
        }
    });

    // Update Array datasets
    cpuData.push(cpuVal); cpuData.shift();
    memData.push(memVal); memData.shift();
    diskData.push(Number(diskRead)); diskData.shift();

    cpuGraph.update();
    memGraph.update();
    diskGraph.update();
}

/**
 * 3. AI / Data Mining Layer (REST / Phase 4 Integration)
 */
document.getElementById('runAnalysisBtn').addEventListener('click', () => {
    
    document.getElementById('analyticsResults').innerHTML = '<p class="placeholder-text">Mining data warehouse arrays... please wait...</p>';
    
    // Fire parallel requests to Data Mining module algorithms
    Promise.all([
        fetch('/api/analytics/cpu').then(r => r.json()),
        fetch('/api/analytics/memory').then(r => r.json())
    ]).then(([cpuResult, memResult]) => {
        renderAnalysis(cpuResult, memResult);
    }).catch(e => {
        document.getElementById('analyticsResults').innerHTML = 
            `<div class="analysis-block" style="border-color: red;">
                <h3>Error Executing Mining Tasks</h3>
                <p>Could not reach the database API route. Check server logs.</p>
            </div>`;
    });
});

function renderAnalysis(cpu, mem) {
    const container = document.getElementById('analyticsResults');
    const cpuClass = cpu.anomalyDetected ? "warning-text" : "safe-text";
    const memClass = mem.pressureState === "HIGH" ? "warning-text" : "safe-text";

    let html = `
        <div class="analysis-block">
            <h3>CPU Load Anomaly Check (Last ${cpu.windowMinutes} Min)</h3>
            <div class="analysis-data">
                <p>Peak Load Detected: <strong>${cpu.maxCpuLoad}</strong></p>
                <p>Average Load: <strong>${cpu.avgCpuLoad}</strong></p>
                <p>Status: <span class="${cpuClass}">${cpu.anomalyDetected ? "CRITICAL OUTLIER" : "STABLE"}</span></p>
                <p style="margin-top: 10px; font-style: italic; color: #8b949e">System AI Recommendation: ${cpu.recommendation}</p>
            </div>
        </div>

        <div class="analysis-block">
            <h3>Memory Footprint Projection (Last ${mem.windowMinutes} Min)</h3>
            <div class="analysis-data">
                <p>Moving Average Ram Utilization: <strong>${mem.averageUsedRamGb}</strong></p>
                <p>Computed Pressure: <span class="${memClass}">${mem.pressureState}</span></p>
            </div>
        </div>
    `;

    container.innerHTML = html;
}

// Start WebSocket Link internally
connectWebsocket();
