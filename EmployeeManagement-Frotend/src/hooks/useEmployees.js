import { useState, useEffect, useCallback } from "react";
import { employeeApi } from "../api/employeeApi";

export function useEmployees(sortBy) {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);
  const [connected, setConnected] = useState(true);

  const fetchAll = useCallback(async () => {
    setLoading(true);
    try {
      const data = await employeeApi.getAll(sortBy || undefined);
      setEmployees(data);
      setConnected(true);
    } catch {
      setConnected(false);
    } finally {
      setLoading(false);
    }
  }, [sortBy]);

  useEffect(() => {
    fetchAll();
  }, [fetchAll]);

  return { employees, loading, connected, refetch: fetchAll };
}
