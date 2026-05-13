import { useState, useEffect } from "react";
import Input from "./Input";
import Select from "./Select";
import Button from "./Button";

const DEPARTMENTS = [
  "Engineering",
  "Marketing",
  "HR",
  "Finance",
  "Design",
  "Operations",
  "Sales",
];

const EMPTY_FORM = {
  firstName: "",
  lastName: "",
  email: "",
  department: "",
};

function validate(form) {
  const errors = {};
  if (!form.firstName || form.firstName.trim().length < 4) {
    errors.firstName = "First name must be at least 4 characters";
  }
  if (!form.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
    errors.email = "A valid email address is required";
  }
  return errors;
}

export default function EmployeeModal({
  mode,        // "add" | "edit"
  employee,    // existing employee when editing
  onSave,      // (formData) => void
  onClose,
  saving,
}) {
  const [form, setForm] = useState(EMPTY_FORM);
  const [errors, setErrors] = useState({});

  // Populate form when editing
  useEffect(() => {
    if (mode === "edit" && employee) {
      setForm({
        firstName:  employee.firstName  || "",
        lastName:   employee.lastName   || "",
        email:      employee.email      || "",
        department: employee.department || "",
      });
    } else {
      setForm(EMPTY_FORM);
    }
    setErrors({});
  }, [mode, employee]);

  const handleChange = (field) => (e) =>
    setForm((prev) => ({ ...prev, [field]: e.target.value }));

  const handleSubmit = () => {
    const errs = validate(form);
    if (Object.keys(errs).length > 0) {
      setErrors(errs);
      return;
    }
    onSave(form);
  };

  return (
    <div
      className="fixed inset-0 z-40 flex items-center justify-center bg-black/70 backdrop-blur-sm"
      onClick={(e) => e.target === e.currentTarget && onClose()}
    >
      <div className="bg-zinc-900 border border-zinc-800 rounded-2xl w-full max-w-md mx-4 shadow-2xl">

        {/* Header */}
        <div className="flex items-start justify-between p-6 border-b border-zinc-800">
          <div>
            <h2 className="text-lg font-bold text-zinc-100">
              {mode === "add" ? "Add New Employee" : "Edit Employee"}
            </h2>
            <p className="text-xs text-zinc-500 mt-1 uppercase tracking-widest">
              {mode === "add"
                ? "Fill in the details below"
                : `Editing record #${employee?.id}`}
            </p>
          </div>
          <button
            onClick={onClose}
            className="text-zinc-500 hover:text-zinc-300 transition-colors text-2xl leading-none -mt-1"
          >
            ×
          </button>
        </div>

        {/* Form Body */}
        <div className="p-6 flex flex-col gap-4">
          <div className="grid grid-cols-2 gap-4">
            <Input
              label="First Name *"
              placeholder="e.g. Alice"
              value={form.firstName}
              onChange={handleChange("firstName")}
              error={errors.firstName}
            />
            <Input
              label="Last Name"
              placeholder="e.g. Smith"
              value={form.lastName}
              onChange={handleChange("lastName")}
            />
          </div>

          <Input
            label="Email *"
            type="email"
            placeholder="alice@company.com"
            value={form.email}
            onChange={handleChange("email")}
            error={errors.email}
          />

          <Select
            label="Department"
            value={form.department}
            onChange={handleChange("department")}
          >
            <option value="">Select a department…</option>
            {DEPARTMENTS.map((d) => (
              <option key={d} value={d}>{d}</option>
            ))}
          </Select>
        </div>

        {/* Footer */}
        <div className="flex justify-end gap-3 p-6 pt-0 border-t border-zinc-800 pt-4">
          <Button variant="ghost" onClick={onClose} disabled={saving}>
            Cancel
          </Button>
          <Button onClick={handleSubmit} loading={saving}>
            {mode === "add" ? "Add Employee" : "Save Changes"}
          </Button>
        </div>
      </div>
    </div>
  );
}
