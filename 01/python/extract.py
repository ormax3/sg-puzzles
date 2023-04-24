import io
import zipfile

def walkZip(inp):
  # walk through zip files while collecting filenames
  names = []
  while True:
    # open as zip file
    with zipfile.ZipFile(inp, 'r') as z:
      # get first and only entry
      name = z.namelist()[0]
      names.append(name)

      # show progress
      #print(name)
      z.printdir()

      # read its content in a buffer used in next iteration
      inp = io.BytesIO(z.read(name))
      if not name.lower().endswith('.zip'):
        # display final text file message
        print(inp.getvalue())
        break
  return names

# recursive walk
names = walkZip(r'../png.zip')
print(f'Depth = {len(names)}')

# combined hex values
s = ''.join(n[:n.rfind('.')] for n in names)
print(s)

# write bytes to output image
with open('png.png', 'wb') as f:
  f.write(bytes.fromhex(s))
