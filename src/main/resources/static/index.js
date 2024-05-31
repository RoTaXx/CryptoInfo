const log = console.log;

const chartProperties = {
    width: 1500,
    height: 600,
    timeScale: {
        timeVisible: true,
        secondsVisible: false,
    }
}

const domElement = document.getElementById('tvchart');
const chart = LightweightCharts.createChart(domElement, chartProperties);
const candleSeries = chart.addCandlestickSeries();

const symbolSelect = document.getElementById('symbol');
const intervalSelect = document.getElementById('interval');

symbolSelect.addEventListener('change', updateChart);
intervalSelect.addEventListener('change', updateChart);

function updateChart() {
    const symbol = symbolSelect.value;
    const interval = intervalSelect.value;

    // Convert interval for the API
    const apiInterval = convertInterval(interval);

    fetch(`https://api.binance.com/api/v3/klines?symbol=${symbol}&interval=${apiInterval}&limit=1000`)
        .then(res => res.json())
        .then(data => {
            const cdata = data.map(d => {
                return { time: d[0] / 1000, open: parseFloat(d[1]), high: parseFloat(d[2]), low: parseFloat(d[3]), close: parseFloat(d[4]) }
            });
            candleSeries.setData(cdata);
        })
        .catch(err => log(err));
}

updateChart();

function convertInterval(interval) {
    switch(interval) {
        case '1m': return '1m';
        case '1d': return '1d';
        case '1w': return '1w';
        case '1M': return '1M';
        default: return '1w';
    }
}