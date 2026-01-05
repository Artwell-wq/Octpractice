# PROJECT 1 — LAN Network Design (Small Business, 2-storey office)

## 1) Project scope and assumptions

### 1.1 Site type
- **Site**: small business office (IT + sales + admin), 2 floors.
- **Goal**: reliable wired LAN for workstations + VoIP/Wi‑Fi + printers + basic servers, with secure segmentation and growth headroom.

### 1.2 Building geometry (used for calculations)
- **Each floor area**: \(30 \text{ m} \times 20 \text{ m} = 600 \text{ m}^2\)
- **Total usable area**: \(2 \times 600 = 1200 \text{ m}^2\)
- **Telecom rooms**:
  - **MDF** (Main Distribution Frame) on Floor 1: network room (rack + ISP demarc + core).
  - **IDF-2** (Intermediate Distribution Frame) on Floor 2: closet above MDF (vertical backbone aligned).

> If your real building has different dimensions/rooms, keep the same method and replace the dimensions/zones (subscriber points and cable length scale linearly with floor area and run distances).

---

## 2) Defining uses (purpose) and applications + numerical estimate

### 2.1 Intended uses
- **Business productivity**: email, SaaS/ERP/CRM, file shares, printing/scanning.
- **Unified communications**: VoIP phones, video meetings.
- **Wireless mobility**: staff/guest Wi‑Fi for laptops/phones.
- **Basic on‑prem services**: authentication, DNS/DHCP, file/print, backups.

### 2.2 User/workload estimate (numerical)
Assume **~80 staff** total, typical busy-hour concurrency **~60 users**.

| Application | Busy-hour concurrent users | Typical per-user rate (avg / peak) | Busy-hour aggregate (rough) |
|---|---:|---:|---:|
| Web/SaaS + email | 50 | 0.5 Mbps / 2 Mbps | 25 Mbps avg, bursts higher |
| File access (SMB) | 20 | 1 Mbps / 10 Mbps (bursty) | ~20 Mbps avg |
| Video meetings (HD) | 10 | 2.0 Mbps / 3.5 Mbps | 20–35 Mbps |
| VoIP (G.711) | 20 calls | ~0.1 Mbps per call | ~2 Mbps |
| Wi‑Fi mobile background | 30 | 0.2 Mbps / 1 Mbps | ~6 Mbps |

**Design implication**:
- **Access**: 1 Gbps to each outlet.
- **Backbone**: **10 Gbps** between switches (MDF ↔ IDF) for aggregation.
- **Internet**: recommended **≥ 500 Mbps** (or 1 Gbps if heavy cloud/video).

---

## 3) Numerical estimation of the network + workstation tasks + architecture selection

### 3.1 Workstations / endpoints (estimate)
- **Workstations**: 80 (mix of desktop + docked laptops)
- **Network printers/MFP**: 6
- **Wi‑Fi APs**: 8 (4 per floor)
- **IP phones** (optional but supported): up to 40 (PoE)
- **Servers**: 2 (virtualization host(s) or 2 small servers)
- **Network/IoT** (optional): 12 (cameras, access control, etc.)

### 3.2 Tasks by workstation type (examples)
- **Office users**: email, web apps, CRM, shared documents, printing.
- **Accounting/HR**: payroll systems, document storage, secure access.
- **IT/admin**: admin tools, remote management, backups, monitoring.
- **Meeting room endpoints**: conferencing bars, screen sharing, guest access.

### 3.3 Architecture selection
**Chosen architecture**: **client–server** (with centralized services) + switched Ethernet access.

**Justification**:
- Central services (AD/LDAP, DNS/DHCP, file/print, backups, monitoring) are easier to secure and administer than peer‑to‑peer.
- Supports **VLAN segmentation**, policy enforcement, and audit logging.
- Scales to growth (more endpoints, Wi‑Fi, VoIP, cameras) without redesigning the whole LAN.

**Peer‑to‑peer** is not selected because it increases operational overhead (permissions, backups, inconsistent configs) and reduces security control in a business environment.

---

## 4) Network topology analysis + transmission standards

### 4.1 Selected topology
**Physical & logical topology**: **hierarchical star** (core/distribution/access).
- End devices connect in a star to access switches (per floor).
- Floor switches uplink (10 Gbps) to core switching in MDF.
- Single routed edge to the ISP via firewall/router.

### 4.2 Logical topology (sketch diagram)

```mermaid
flowchart TB
  ISP[(ISP / ONT)]
  FW[Firewall / Router\n(NAT, VPN, ACLs)]
  CORE[Core L3 Switch (MDF)\nInter‑VLAN routing]
  SW1[Access Switch Stack - Floor 1 (PoE)]
  SW2[Access Switch Stack - Floor 2 (PoE)]
  SRV[Servers\n(AD/DNS/DHCP/File/Backup)]
  AP1[Wi‑Fi APs\n(802.11ax)]
  USERS1[Wired endpoints\nPCs/Printers/Phones]
  USERS2[Wired endpoints\nPCs/Printers/Phones]

  ISP --> FW --> CORE
  SRV --- CORE
  CORE == "10Gb (fiber)" ==> SW1
  CORE == "10Gb (fiber)" ==> SW2
  SW1 --- AP1
  SW1 --- USERS1
  SW2 --- AP1
  SW2 --- USERS2
```

### 4.3 Transmission standards
- **Wired access**: IEEE **802.3ab (1000BASE‑T)** over copper (Cat6/Cat6A).
- **Backbone (recommended)**: IEEE **802.3ae/802.3ba** (10G via fiber, e.g., **10GBASE‑SR** on OM4).
- **Wi‑Fi**: IEEE **802.11ax (Wi‑Fi 6)**; WPA2‑Enterprise/WPA3 where supported.
- **Switching**: IEEE **802.1Q VLAN**, **802.1p QoS**, **802.1X** (optional NAC), **RSTP** for loop protection.

---

## 5) IP plan, VLANs, and security (logical design)

### 5.1 VLANs (example)
| VLAN | Name | Subnet | Typical devices | Notes |
|---:|---|---|---|---|
| 10 | STAFF | 10.10.10.0/24 | employee PCs | standard access |
| 20 | VOICE | 10.10.20.0/24 | IP phones | QoS + restricted |
| 30 | SERVERS | 10.10.30.0/24 | AD/DNS/DHCP/file | limited admin access |
| 40 | PRINTERS | 10.10.40.0/24 | printers/MFP | allow from STAFF |
| 50 | GUEST | 10.10.50.0/24 | guest Wi‑Fi | Internet only |
| 60 | MGMT | 10.10.60.0/24 | switch/AP mgmt | IT only |

### 5.2 Core policies (summary)
- **Inter‑VLAN routing** on L3 core switch; **default route** to firewall.
- **Firewall**:
  - Guest VLAN → Internet only (block internal RFC1918).
  - Mgmt VLAN restricted to IT admin hosts.
  - Printers reachable from STAFF only.
- **QoS**: prioritize voice (DSCP EF) and video.
- **DHCP**: per‑VLAN scopes; reservations for infrastructure.

---

## 6) Site drawing + structured cabling plan (physical design)

### 6.1 Distribution points (cabinets) and outlets
- **MDF (Floor 1)**: 42U rack in network room (core switch, firewall, servers, patch panels, UPS).
- **IDF-2 (Floor 2)**: 24U wall/standing rack (access switches, patch panels, UPS).
- **Subscriber outlet**: **double RJ45** at each subscriber point (common office practice: 2 ports per location).
- **Patch panels**: 48‑port panels in each rack.

### 6.2 Subscriber point calculation (given rule: 1 point per 10 m²)
Rule: **1 subscriber point / 10 m²**.

- Floor 1: \(600 / 10 = 60\) subscriber points
- Floor 2: \(600 / 10 = 60\) subscriber points
- **Total**: **120 subscriber points**

If each point is a **double RJ45**:
- **RJ45 ports required**: \(120 \times 2 = 240\) ports

> The assignment text mentions “RJ45 sockets and DATA mains socket”. In practice this means each subscriber point has at least one **RJ45 data outlet** and an **adjacent power outlet**; this design provides **two RJ45** per point for flexibility (PC + phone, PC + printer, docking + spare, etc.).

### 6.3 Logical-to-physical mapping (zones)
For cable estimation, each floor is divided into 4 work zones + meeting/support areas. Outlets are evenly distributed:
- **Per floor**: 60 points = 15 points per zone (North‑West, North‑East, South‑West, South‑East).
- **MDF/IDF position**: near geometric center of the floor (minimizes average run length).

### 6.4 Floor plan sketches (not to scale)

#### Floor 1 (MDF + offices)

```text
30 m  ┌──────────────────────────────────────────────┐
      │ NW Zone (15 pts)   │ Corridor │ NE Zone(15) │
      │                    │         │             │
20 m  │───────────────┐    │         │    ┌────────│
      │ Meeting room  │    │         │    │ Open   │
      │ (6 pts)       │    │         │    │ office │
      │───────────────┘    │         │    │ (9 pts)│
      │ SW Zone (15 pts)   │  MDF    │ SE Zone(15) │
      │                    │(Rack)   │             │
      └──────────────────────────────────────────────┘
            Cable tray route runs along corridor perimeter
```

#### Floor 2 (IDF-2 + offices)

```text
30 m  ┌──────────────────────────────────────────────┐
      │ NW Zone (15 pts)   │ Corridor │ NE Zone(15) │
      │                    │         │             │
20 m  │───────────────┐    │         │    ┌────────│
      │ Training room │    │         │    │ Open   │
      │ (10 pts)      │    │         │    │ office │
      │───────────────┘    │         │    │ (5 pts)│
      │ SW Zone (15 pts)   │ IDF-2   │ SE Zone(15) │
      │                    │(Rack)   │             │
      └──────────────────────────────────────────────┘
            Cable tray route runs along corridor perimeter
```

### 6.5 Horizontal cabling plan (cable track route)
**Concept**: structured cabling with a main corridor cable tray (over corridor ceiling) and short branch conduits to outlets.
- **From MDF/IDF**: cables go to corridor tray → branch to room → wall outlet.
- Keep data cables separated from power where required; cross power at 90°.
- Label both ends (panel port and outlet ID).

### 6.6 Vertical projection (multi-storey vertical runs)
Vertical backbone between MDF (Floor 1) and IDF‑2 (Floor 2) via a riser:
- **Fiber**: OM4 multimode, **12‑core** (future expansion; only 2–4 fibers initially used).
- **Copper** (optional redundancy/PoE uplink not typical): Cat6A riser cable (1–2 runs) for backup management link.

---

## 7) Required cable length calculation

### 7.1 Design limits and recommendations
- **Permanent link max**: 90 m (horizontal cabling) + patch cords up to 10 m total (typical structured cabling rule).
- This design targets **≤ 60 m average** and **≤ 80 m worst‑case** per outlet by centering MDF/IDF.

### 7.2 Estimation method (transparent)
For each floor:
- 60 subscriber points (120 cable runs if two RJ45 per point).
- Estimate average permanent‑link length per run using zones and corridor routing.

Assumptions for run lengths (per floor, per cable run):
- NW zone average: 35 m
- NE zone average: 35 m
- SW zone average: 30 m
- SE zone average: 30 m
- Meeting/training support points included within zones (averages already account for them).

Add installation allowances:
- **Slack**: +10% for routing, service loops, and wastage.

### 7.3 Cable length per floor
Per floor:
- Points per zone: 15 points
- Cable runs per point: 2 (double RJ45)
- Runs per zone: 15 × 2 = 30 runs

Floor 1 permanent‑link cable:
- NW: 30 × 35 m = 1050 m
- NE: 30 × 35 m = 1050 m
- SW: 30 × 30 m = 900 m
- SE: 30 × 30 m = 900 m
- **Subtotal**: 3900 m
- **+10% slack**: 390 m
- **Floor 1 total**: **4290 m**

Floor 2 permanent‑link cable (same zoning):
- Subtotal: 3900 m
- +10% slack: 390 m
- **Floor 2 total**: **4290 m**

### 7.4 Total horizontal cable (both floors)
- **Horizontal (permanent links)**: 4290 m + 4290 m = **8580 m** of twisted‑pair cable.

### 7.5 Backbone cable length (vertical)
Assume riser path length (including routing and slack): **25 m**.
- OM4 12‑core fiber: **25 m**
- Optional Cat6A riser: **25 m**

### 7.6 Patch cords (not permanent link)
Typical patching (not included in 90 m permanent link):
- Rack patch cords: 2 m each × 240 ports ≈ 480 m
- User patch cords: 3 m each × 120 points ≈ 360 m

> You can list patch cords separately in the cost estimate; they’re usually not counted as “installed cable length” but are required for operation.

---
