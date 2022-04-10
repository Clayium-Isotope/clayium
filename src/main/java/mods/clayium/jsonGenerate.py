import json

rootPath = r'D:/clayium/src/main/resources/assets/clayium'


def gen_state(tier, kind):
    file = open(rootPath + r'/blockstates/' + tier2prefix(tier) + r'_' + kind + r'.json', 'w')
    file.write(json.dumps({
        'multipart': [
            {
                'apply': { 'model': 'clayium:machine_hull_' + str(tier - 1) }
            },
            {
                'when': { 'facing': 'north' },
                'apply': { 'model': 'clayium:machine/' + kind, 'y': 0 }
            },
            {
                'when': { 'facing': 'east' },
                'apply': { 'model': 'clayium:machine/' + kind, 'y': 90 }
            },
            {
                'when': { 'facing': 'south' },
                'apply': { 'model': 'clayium:machine/' + kind, 'y': 180 }
            },
            {
                'when': { 'facing': 'west' },
                'apply': { 'model': 'clayium:machine/' + kind, 'y': 270 }
            }
        ]
    }, indent=2))
    file.close()


def gen_machine(kind, tiers):
    for tier in tiers:
        gen_state(tier, kind)


def tier2prefix(tier):
    if tier == 0: return ''
    if tier == 1: return 'clay'
    if tier == 2: return 'dense_clay'
    if tier == 3: return 'simple'
    if tier == 4: return 'basic'
    if tier == 5: return 'advanced'
    if tier == 6: return 'precision'
    if tier == 7: return 'clay_steel'
    if tier == 8: return 'clayium'
    if tier == 9: return 'ultimate'
    if tier == 10: return 'antimatter'
    if tier == 11: return 'pure_antimatter'
    if tier == 12: return 'oec'
    if tier == 13: return 'opa'
    return ''


def main():
    gen_machine('bending_machine', [ 1, 2, 3, 4, 5, 6, 7, 9 ])
    gen_machine('wire_drawing_machine', [ 1, 2, 3, 4 ])
    gen_machine('pipe_drawing_machine', [ 1, 2, 3, 4 ])
    gen_machine('cutting_machine', [ 1, 2, 3, 4 ])
    gen_machine('lathe', [ 1, 2, 3, 4 ])
    gen_machine('milling_machine', [ 1, 3, 4 ])


main()
