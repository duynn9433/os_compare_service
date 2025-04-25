-- =========================
-- Configurable Parameters
-- =========================
local test_name = "RD02-write-mariadb"  -- Có thể cấu hình qua biến môi trường
local output_file_path = "wrk_result.csv"  -- Tên file CSV
local start_time_utc = os.date("!%Y-%m-%dT%H:%M:%SZ")  -- ISO 8601 UTC

-- Seed cho random request
math.randomseed(os.time())

-- Tạo request GET với tham số ngẫu nhiên
request = function()
    local a = math.random(1, 100000)
    local path = "/rdb/write-mariadb"
    return wrk.format("GET", path)
end

-- Kết thúc test, hiển thị & ghi log
done = function(summary, latency, requests)
   local end_time_utc = os.date("!%Y-%m-%dT%H:%M:%SZ")
   local total_request = summary.requests
   local duration_sec = summary.duration / 1000000
   local total_transfer_gb = summary.bytes * 8 / (1024^3)
   local avg_latency_ms = latency.mean / 1000
   local max_latency_ms = latency.max / 1000
   local stddev_latency_ms = latency.stdev / 1000

   local percentiles = {}
   for _, p in ipairs({ 50, 90, 95, 97, 99, 99.99 }) do
      percentiles[tostring(p)] = latency:percentile(p) / 1000
   end

   local connect_errors = summary.errors.connect or 0
   local read_errors = summary.errors.read or 0
   local write_errors = summary.errors.write or 0
   local http_status_errors = summary.errors.status or 0
   local timeout_errors = summary.errors.timeout or 0

   local total_errors = connect_errors + read_errors + write_errors + http_status_errors + timeout_errors
   local total_request_with_err = total_request + total_errors
   local error_percentage = (total_errors / total_request_with_err) * 100
   local rps = total_request / duration_sec
   local tps = summary.bytes / duration_sec
   local tps_gb = summary.bytes * 8 / (duration_sec * 1024^3)

   -- In ra terminal
   io.write("------------------------------\n")
   print("TestName:  " .. test_name)
   print("StartTime: " .. start_time_utc)
   print("EndTime:   " .. end_time_utc)
   print("TotalRequests: " .. total_request)
   print("TotalTime: " .. string.format("%.3f", duration_sec) .. " s")
   print("TotalTransfer: " .. string.format("%.3f", total_transfer_gb) .. " Gb")
   print("Average: " .. string.format("%.3f", avg_latency_ms) .. " ms")
   print("Maximum: " .. string.format("%.3f", max_latency_ms) .. " ms")
   print("StandardDeviation: " .. string.format("%.3f", stddev_latency_ms) .. " ms")
   print("Latency Distribution:")
   for k, v in pairs(percentiles) do
      print(k .. "%\t" .. string.format("%.3f", v) .. " ms")
   end
   print("%Error: " .. string.format("%.3f", error_percentage) .. " %")
   print("RequestsPerSecond: " .. string.format("%.3f", rps))
   print("TransferPerSecondRaw: " .. string.format("%.3f", tps) .. " bytes")
   print("TransferPerSecond: " .. string.format("%.3f", tps_gb) .. " Gb/s")

   -- Ghi file CSV
   local file = io.open(output_file_path, "a")
   if file then
      -- Nếu file trống, ghi header
      local attr = io.popen("test -s " .. output_file_path .. " && echo nonempty || echo empty"):read("*a")
      if string.find(attr, "empty") then
         file:write("TestName,StartTime,EndTime,TotalRequests,TotalTime(s),TotalTransfer(Gb),Average(ms),Maximum(ms),StandardDeviation(ms),")
         file:write("P50(ms),P90(ms),P95(ms),P97(ms),P99(ms),P99.99(ms),Error(%),RPS,TransferRaw(Bytes),Transfer(Gb/s)\n")
      end

      file:write(string.format("%s,%s,%s,%d,%.3f,%.3f,%.3f,%.3f,%.3f,",
         test_name, start_time_utc, end_time_utc, total_request, duration_sec, total_transfer_gb,
         avg_latency_ms, max_latency_ms, stddev_latency_ms))
      file:write(string.format("%.3f,%.3f,%.3f,%.3f,%.3f,%.3f,",
         percentiles["50"], percentiles["90"], percentiles["95"], percentiles["97"], percentiles["99"], percentiles["99.99"]))
      file:write(string.format("%.3f,%.3f,%.3f,%.3f\n", error_percentage, rps, tps, tps_gb))
      file:close()
   else
      print("❌ Không thể ghi file log: " .. output_file_path)
   end
end