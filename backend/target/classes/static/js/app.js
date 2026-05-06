function fillLocation() {
  if (!navigator.geolocation) {
    alert('Geolocation is not supported');
    return;
  }
  navigator.geolocation.getCurrentPosition(function(pos) {
    document.getElementById('lat').value = pos.coords.latitude;
    document.getElementById('lng').value = pos.coords.longitude;
  }, function(err) {
    alert('Location error: ' + err.message);
  });
}

let qrScannerInstance = null;
let qrScannerVisible = false;

function setQrScannerStatus(message) {
  const status = document.getElementById('qrScannerStatus');
  if (status) {
    status.textContent = message;
  }
}

function toggleQrScanner() {
  const panel = document.getElementById('qrScannerPanel');
  const toggleButton = document.getElementById('toggleScannerBtn');

  if (!panel || !toggleButton) {
    return;
  }

  qrScannerVisible = panel.classList.contains('d-none');
  panel.classList.toggle('d-none');
  toggleButton.textContent = qrScannerVisible ? 'Hide QR Scanner' : 'Scan QR Code';

  if (!panel.classList.contains('d-none')) {
    setQrScannerStatus('Open the scanner and point your camera at the faculty QR code.');
  } else {
    stopQrScanner();
  }
}

function startQrScanner() {
  const reader = document.getElementById('qrReader');
  const startButton = document.getElementById('startScannerBtn');
  const stopButton = document.getElementById('stopScannerBtn');

  if (!reader || typeof Html5Qrcode === 'undefined') {
    setQrScannerStatus('QR scanner is not available right now.');
    return;
  }

  if (qrScannerInstance) {
    return;
  }

  setQrScannerStatus('Starting camera...');
  qrScannerInstance = new Html5Qrcode('qrReader');

  Html5Qrcode.getCameras().then(function(cameras) {
    if (!cameras || cameras.length === 0) {
      setQrScannerStatus('No camera found on this device.');
      qrScannerInstance = null;
      return;
    }

    const cameraId = cameras[0].id;
    qrScannerInstance.start(
      cameraId,
      { fps: 10, qrbox: 220 },
      function(decodedText) {
        const tokenField = document.getElementById('qrToken');
        if (tokenField) {
          tokenField.value = decodedText;
        }
        setQrScannerStatus('QR scanned successfully. Token added to the form.');
        fillLocation();
        stopQrScanner();
      }
    ).then(function() {
      if (startButton) {
        startButton.disabled = true;
      }
      if (stopButton) {
        stopButton.disabled = false;
      }
      setQrScannerStatus('Scanner started. Point your camera at the QR code.');
    }).catch(function(error) {
      setQrScannerStatus('Failed to start scanner: ' + error);
      qrScannerInstance = null;
    });
  }).catch(function(error) {
    setQrScannerStatus('Camera access failed: ' + error);
    qrScannerInstance = null;
  });
}

function stopQrScanner() {
  const startButton = document.getElementById('startScannerBtn');
  const stopButton = document.getElementById('stopScannerBtn');

  if (!qrScannerInstance) {
    if (startButton) {
      startButton.disabled = false;
    }
    if (stopButton) {
      stopButton.disabled = true;
    }
    return;
  }

  qrScannerInstance.stop().then(function() {
    return qrScannerInstance.clear();
  }).catch(function() {
    return null;
  }).finally(function() {
    qrScannerInstance = null;
    if (startButton) {
      startButton.disabled = false;
    }
    if (stopButton) {
      stopButton.disabled = true;
    }
  });
}
