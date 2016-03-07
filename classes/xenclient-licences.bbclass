# Generates a CSV file containing a list of the packages installed in an image
# and their licensing information. For each package it shows:
#
#   - package name
#   - description
#   - version
#   - licence
#   - home page
#   - source
#
# Packages can be excluded via the 'exclude_re' variable below.

python do_licences() {
    import glob, operator, os, re, sys

    fields = ('Package', 'Description', 'Version', 'License',
              'Homepage', 'Source')

    headings = ('Package name', 'Description', 'Version', 'Licence',
                'Home page', 'Source')

    exclude_re = re.compile('^packagegroup-|-depends$|-feed-configs$')

    split_re = re.compile(': ')

    # Read package info from the /var/lib/opkg/info directory within the image

    info_dir = d.getVar('IMAGE_ROOTFS', d, 1) + '/var/lib/opkg/info'

    packages = []

    for input_file in glob.glob(info_dir + '/*.control'):
        file = open(input_file)

        package = {}

        # Supply an empty default value for optional field:
        package['Homepage'] = ''

        for line in file:
            if line and line[0] == ' ':
                # Only take the first line of multi-line fields
                continue

            key, value = split_re.split(line.rstrip(), 1)
            if key in fields:
                package[key] = value

        if not exclude_re.search(package['Package']):
            packages.append(package)

        file.close()

    # Write package information to CSV file

    packages.sort(key=operator.itemgetter('Package'))

    output_file = \
        d.getVar('DEPLOY_DIR_IMAGE', d, 1) + '/' + \
        d.getVar('IMAGE_BASENAME', d, 1) + '-licences.csv'

    file = open(output_file, 'w')

    def write_csv(file, values):
        file.write(','.join('"' + v.replace('"', '""') + '"' \
                            for v in values) + '\n')

    write_csv(file, headings)

    for package in packages:
        write_csv(file, (package[f] for f in fields))

    file.close()
}

addtask licences after do_rootfs before do_build
